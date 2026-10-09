package com.college.labtracker.dto.session;

import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

// DTO for grouping students into a practical work team for a session
public class TeamCreateRequest {

    @NotBlank(message = "Team name is required (e.g. Team-1)")
    private String teamName;

    @NotEmpty(message = "At least one student member is required")
    private Set<Long> memberIds = new HashSet<>();

    public TeamCreateRequest() {
    }

    public TeamCreateRequest(String teamName, Set<Long> memberIds) {
        this.teamName = teamName;
        this.memberIds = memberIds;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Set<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<Long> memberIds) {
        this.memberIds = memberIds;
    }
}
