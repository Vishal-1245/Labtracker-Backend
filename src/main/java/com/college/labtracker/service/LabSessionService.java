package com.college.labtracker.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.session.AttendanceRecordRequest;
import com.college.labtracker.dto.session.AttendanceResponse;
import com.college.labtracker.dto.session.AttendanceSubmitRequest;
import com.college.labtracker.dto.session.LabSessionRequest;
import com.college.labtracker.dto.session.LabSessionResponse;
import com.college.labtracker.dto.session.TeamCreateRequest;
import com.college.labtracker.dto.session.TeamResponse;
import com.college.labtracker.dto.user.UserResponse;
import com.college.labtracker.entity.Attendance;
import com.college.labtracker.entity.Experiment;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.entity.LabSession;
import com.college.labtracker.entity.SessionStatus;
import com.college.labtracker.entity.Team;
import com.college.labtracker.entity.User;
import com.college.labtracker.exception.InvalidBookingException;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.AttendanceRepository;
import com.college.labtracker.repository.ExperimentRepository;
import com.college.labtracker.repository.LabRepository;
import com.college.labtracker.repository.LabSessionRepository;
import com.college.labtracker.repository.TeamRepository;
import com.college.labtracker.repository.UserRepository;

// Service coordinating scheduled lab classes, student teams, and attendance records
@Service
public class LabSessionService {

    private final LabSessionRepository sessionRepository;
    private final AttendanceRepository attendanceRepository;
    private final TeamRepository teamRepository;
    private final LabRepository labRepository;
    private final ExperimentRepository experimentRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public LabSessionService(
            LabSessionRepository sessionRepository,
            AttendanceRepository attendanceRepository,
            TeamRepository teamRepository,
            LabRepository labRepository,
            ExperimentRepository experimentRepository,
            UserRepository userRepository,
            UserService userService
    ) {
        this.sessionRepository = sessionRepository;
        this.attendanceRepository = attendanceRepository;
        this.teamRepository = teamRepository;
        this.labRepository = labRepository;
        this.experimentRepository = experimentRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public LabSessionResponse toResponse(LabSession s) {
        return new LabSessionResponse(
                s.getId(),
                s.getLab().getId(),
                s.getLab().getName(),
                s.getExperiment().getId(),
                s.getExperiment().getCode(),
                s.getExperiment().getTitle(),
                s.getSessionDate(),
                s.getStartTime(),
                s.getEndTime(),
                s.getBatch(),
                s.getConductedBy().getId(),
                s.getConductedBy().getFullName(),
                s.getStatus()
        );
    }

    public AttendanceResponse toAttendanceResponse(Attendance a) {
        return new AttendanceResponse(
                a.getId(),
                a.getSession().getId(),
                a.getStudent().getId(),
                a.getStudent().getUsername(),
                a.getStudent().getFullName(),
                a.isPresent()
        );
    }

    public TeamResponse toTeamResponse(Team team) {
        List<UserResponse> memberResponses = team.getMembers().stream()
                .map(userService::toResponse)
                .collect(Collectors.toList());
        return new TeamResponse(
                team.getId(),
                team.getSession().getId(),
                team.getTeamName(),
                memberResponses
        );
    }

    // List lab sessions with optional filters
    public List<LabSessionResponse> getAllSessions(Long labId, LocalDate date, SessionStatus status) {
        List<LabSession> list = sessionRepository.findAll();

        if (labId != null) {
            list = list.stream().filter(s -> s.getLab().getId().equals(labId)).collect(Collectors.toList());
        }
        if (date != null) {
            list = list.stream().filter(s -> s.getSessionDate().equals(date)).collect(Collectors.toList());
        }
        if (status != null) {
            list = list.stream().filter(s -> s.getStatus() == status).collect(Collectors.toList());
        }

        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Retrieve session by ID
    public LabSessionResponse getSessionById(Long id) {
        LabSession s = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with ID: " + id));
        return toResponse(s);
    }

    // Schedule a new laboratory class session
    @Transactional
    public LabSessionResponse createSession(LabSessionRequest request, String currentUsername) {
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new InvalidBookingException("Session start time must be before end time");
        }

        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));

        Experiment experiment = experimentRepository.findById(request.getExperimentId())
                .orElseThrow(() -> new ResourceNotFoundException("Experiment not found with ID: " + request.getExperimentId()));

        User conductedBy;
        if (request.getConductedById() != null) {
            conductedBy = userRepository.findById(request.getConductedById())
                    .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with ID: " + request.getConductedById()));
        } else {
            conductedBy = userRepository.findByUsername(currentUsername)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + currentUsername));
        }

        LabSession session = new LabSession(
                lab,
                experiment,
                request.getSessionDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getBatch(),
                conductedBy,
                request.getStatus() != null ? request.getStatus() : SessionStatus.OPEN
        );

        LabSession saved = sessionRepository.save(session);
        return toResponse(saved);
    }

    // Update status (OPEN -> IN_PROGRESS -> COMPLETED -> CANCELLED)
    @Transactional
    public LabSessionResponse updateStatus(Long id, SessionStatus status) {
        LabSession session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with ID: " + id));
        session.setStatus(status);
        LabSession updated = sessionRepository.save(session);
        return toResponse(updated);
    }

    // Record attendance for students participating in this session
    @Transactional
    public List<AttendanceResponse> submitAttendance(Long sessionId, AttendanceSubmitRequest request) {
        LabSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with ID: " + sessionId));

        List<AttendanceResponse> results = new ArrayList<>();
        for (AttendanceRecordRequest rec : request.getRecords()) {
            User student = userRepository.findById(rec.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + rec.getStudentId()));

            Attendance attendance = attendanceRepository.findBySessionIdAndStudentId(sessionId, student.getId())
                    .orElse(new Attendance(session, student, rec.isPresent()));

            attendance.setPresent(rec.isPresent());
            Attendance saved = attendanceRepository.save(attendance);
            results.add(toAttendanceResponse(saved));
        }
        return results;
    }

    // Fetch attendance log for a session
    public List<AttendanceResponse> getAttendance(Long sessionId) {
        return attendanceRepository.findBySessionId(sessionId).stream()
                .map(this::toAttendanceResponse)
                .collect(Collectors.toList());
    }

    // Group students into a team for this lab session
    @Transactional
    public TeamResponse createTeam(Long sessionId, TeamCreateRequest request) {
        LabSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Lab session not found with ID: " + sessionId));

        Set<User> members = new HashSet<>();
        for (Long memberId : request.getMemberIds()) {
            User student = userRepository.findById(memberId)
                    .orElseThrow(() -> new ResourceNotFoundException("Student member not found with ID: " + memberId));
            members.add(student);
        }

        Team team = new Team(session, request.getTeamName(), members);
        Team saved = teamRepository.save(team);
        return toTeamResponse(saved);
    }

    // Retrieve teams formed in this session
    public List<TeamResponse> getTeams(Long sessionId) {
        return teamRepository.findBySessionId(sessionId).stream()
                .map(this::toTeamResponse)
                .collect(Collectors.toList());
    }
}
