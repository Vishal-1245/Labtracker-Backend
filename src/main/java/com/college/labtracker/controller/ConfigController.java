package com.college.labtracker.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.college.labtracker.dto.config.ConfigResponse;

// Public controller exposing institutional college context to the frontend application
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Value("${app.college-name:Anurag University}")
    private String collegeName;

    @Value("${app.department-name:Department of Electronics and Communication Engineering}")
    private String departmentName;

    @Value("${app.rollno-pattern:^[0-9]{2}[a-zA-Z]{2}[0-9]{3}[a-zA-Z][0-9]{2}$}")
    private String rollNoPattern;

    // GET /api/config/public: Exposes institutional details for frontend navbar, header, and login branding
    @GetMapping("/public")
    public ResponseEntity<ConfigResponse> getPublicConfig() {
        ConfigResponse response = new ConfigResponse(collegeName, departmentName, rollNoPattern);
        return ResponseEntity.ok(response);
    }
}
