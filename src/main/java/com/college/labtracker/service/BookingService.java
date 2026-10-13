package com.college.labtracker.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.booking.BookingRequest;
import com.college.labtracker.dto.booking.BookingResponse;
import com.college.labtracker.entity.Booking;
import com.college.labtracker.entity.BookingStatus;
import com.college.labtracker.entity.Equipment;
import com.college.labtracker.entity.EquipmentStatus;
import com.college.labtracker.entity.Experiment;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.entity.Role;
import com.college.labtracker.entity.User;
import com.college.labtracker.exception.EquipmentUnderMaintenanceException;
import com.college.labtracker.exception.InvalidBookingException;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.exception.SlotAlreadyBookedException;
import com.college.labtracker.repository.BookingRepository;
import com.college.labtracker.repository.EquipmentRepository;
import com.college.labtracker.repository.ExperimentRepository;
import com.college.labtracker.repository.LabRepository;
import com.college.labtracker.repository.UserRepository;

// Service handling slot reservations, apparatus booking conflicts, and capacity enforcement
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final LabRepository labRepository;
    private final ExperimentRepository experimentRepository;
    private final EquipmentRepository equipmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public BookingService(
            BookingRepository bookingRepository,
            LabRepository labRepository,
            ExperimentRepository experimentRepository,
            EquipmentRepository equipmentRepository,
            UserRepository userRepository,
            NotificationService notificationService
    ) {
        this.bookingRepository = bookingRepository;
        this.labRepository = labRepository;
        this.experimentRepository = experimentRepository;
        this.equipmentRepository = equipmentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public BookingResponse toResponse(Booking b) {
        return new BookingResponse(
                b.getId(),
                b.getStudent().getId(),
                b.getStudent().getUsername(),
                b.getStudent().getFullName(),
                b.getLab().getId(),
                b.getLab().getName(),
                b.getExperiment() != null ? b.getExperiment().getId() : null,
                b.getExperiment() != null ? b.getExperiment().getCode() : null,
                b.getExperiment() != null ? b.getExperiment().getTitle() : null,
                b.getEquipment() != null ? b.getEquipment().getId() : null,
                b.getEquipment() != null ? b.getEquipment().getName() : null,
                b.getEquipment() != null ? b.getEquipment().getAssetTag() : null,
                b.getBookingDate(),
                b.getStartTime(),
                b.getEndTime(),
                b.getPurpose(),
                b.getStatus()
        );
    }

    // Create a new lab or equipment reservation with strict conflict and capacity checks
    @Transactional
    public BookingResponse createBooking(BookingRequest request, String currentUsername) {
        // 1. Validate time bounds
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new InvalidBookingException("Booking start time must be before end time");
        }

        // 2. Validate date is not in the past
        if (request.getBookingDate().isBefore(LocalDate.now())) {
            throw new InvalidBookingException("Booking date cannot be in the past");
        }

        // 3. Resolve student
        User student;
        if (request.getStudentId() != null) {
            student = userRepository.findById(request.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));
        } else {
            student = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("Logged in user not found: " + currentUsername));
        }

        // 4. Resolve lab and check slot capacity
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));

        long overlappingLabBookings = bookingRepository.countOverlappingLabBookings(
                lab.getId(),
                request.getBookingDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (overlappingLabBookings >= lab.getSlotCapacity()) {
            throw new SlotAlreadyBookedException("Lab slot capacity (" + lab.getSlotCapacity() 
                    + ") reached for " + lab.getName() + " on " + request.getBookingDate() 
                    + " between " + request.getStartTime() + " and " + request.getEndTime());
        }

        // 5. Resolve experiment (optional)
        Experiment experiment = null;
        if (request.getExperimentId() != null) {
            experiment = experimentRepository.findById(request.getExperimentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + request.getExperimentId()));
        }

        // 6. Resolve equipment (optional) with maintenance and collision checks
        Equipment equipment = null;
        if (request.getEquipmentId() != null) {
            equipment = equipmentRepository.findById(request.getEquipmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + request.getEquipmentId()));

            if (equipment.getStatus() == EquipmentStatus.UNDER_MAINTENANCE) {
                throw new EquipmentUnderMaintenanceException("Equipment '" + equipment.getName() 
                        + "' (Tag: " + equipment.getAssetTag() + ") is currently under maintenance and cannot be booked");
            }

            long overlappingEquipmentBookings = bookingRepository.countOverlappingEquipmentBookings(
                    equipment.getId(),
                    request.getBookingDate(),
                    request.getStartTime(),
                    request.getEndTime()
            );

            if (overlappingEquipmentBookings > 0) {
                throw new SlotAlreadyBookedException("Equipment '" + equipment.getName() 
                        + "' (Tag: " + equipment.getAssetTag() + ") is already reserved by another student for this time slot");
            }
        }

        // 7. Save and return booking
        Booking booking = new Booking(
                student,
                lab,
                experiment,
                equipment,
                request.getBookingDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getPurpose(),
                BookingStatus.BOOKED
        );

        Booking saved = bookingRepository.save(booking);
        notificationService.notifyBookingCreated(saved);
        return toResponse(saved);
    }

    // Retrieve bookings submitted by currently logged-in student
    public List<BookingResponse> getMyBookings(String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));
        return bookingRepository.findByStudentIdOrderByBookingDateDescStartTimeDesc(user.getId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // Retrieve all bookings with optional filters (In-charge and Admin)
    public List<BookingResponse> getAllBookings(Long labId, LocalDate date, BookingStatus status, String currentUsername) {
        List<Booking> list = bookingRepository.findAll();
        Long effectiveLabId = labId;

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));
        if (currentUser.getRole() == Role.LAB_ASSISTANT) {
            if (currentUser.getLab() == null) {
                return List.of();
            }
            effectiveLabId = currentUser.getLab().getId();
        }

        Long filterLabId = effectiveLabId;
        if (filterLabId != null) {
            list = list.stream().filter(b -> b.getLab().getId().equals(filterLabId)).collect(Collectors.toList());
        }
        if (date != null) {
            list = list.stream().filter(b -> b.getBookingDate().equals(date)).collect(Collectors.toList());
        }
        if (status != null) {
            list = list.stream().filter(b -> b.getStatus() == status).collect(Collectors.toList());
        }

        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Cancel a booking
    @Transactional
    public BookingResponse cancelBooking(Long id, String currentUsername) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));

        // Students can only cancel their own bookings
        if (currentUser.getRole() == Role.STUDENT && !booking.getStudent().getId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Access Denied: You cannot cancel another student's reservation");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);
        return toResponse(updated);
    }

    // Mark booking as completed (In-charge and Admin)
    @Transactional
    public BookingResponse completeBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        booking.setStatus(BookingStatus.COMPLETED);
        Booking updated = bookingRepository.save(booking);
        return toResponse(updated);
    }
}
