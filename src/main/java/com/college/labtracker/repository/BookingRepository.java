package com.college.labtracker.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.college.labtracker.entity.Booking;
import com.college.labtracker.entity.BookingStatus;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStudentIdOrderByBookingDateDescStartTimeDesc(Long studentId);

    List<Booking> findByBookingDate(LocalDate bookingDate);

    List<Booking> findByStatus(BookingStatus status);

    // Count overlapping BOOKED slots for the same lab (overlap: existing.start < new.end AND existing.end > new.start)
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.lab.id = :labId AND b.bookingDate = :bookingDate AND b.status = com.college.labtracker.entity.BookingStatus.BOOKED AND b.startTime < :endTime AND b.endTime > :startTime")
    long countOverlappingLabBookings(
        @Param("labId") Long labId,
        @Param("bookingDate") LocalDate bookingDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );

    // Check if equipment has an overlapping BOOKED slot
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.equipment.id = :equipmentId AND b.bookingDate = :bookingDate AND b.status = com.college.labtracker.entity.BookingStatus.BOOKED AND b.startTime < :endTime AND b.endTime > :startTime")
    long countOverlappingEquipmentBookings(
        @Param("equipmentId") Long equipmentId,
        @Param("bookingDate") LocalDate bookingDate,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );
}
