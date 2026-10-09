package com.college.labtracker.dto.session;

import java.util.ArrayList;
import java.util.List;
import com.college.labtracker.dto.user.UserResponse;

// DTO representing a lab work team and its enrolled students
public class TeamResponse {

    private Long id;
    private Long sessionId;
    private String teamName;
    private List<UserResponse> members = new ArrayList<>();

    public TeamResponse() {
    }

    public TeamResponse(Long id, Long sessionId, String teamName, List<UserResponse> members) {
        this.id = id;
        this.sessionId = sessionId;
        this.teamName = teamName;
        this.members = members != null ? members : new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public List<UserResponse> getMembers() {
        return members;
    }

    public void setMembers(List<UserResponse> members) {
        this.members = members;
    }
}
