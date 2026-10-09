package com.college.labtracker.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.college.labtracker.dto.component.ComponentIssueRequest;
import com.college.labtracker.dto.component.ComponentIssueResponse;
import com.college.labtracker.dto.component.ComponentRequest;
import com.college.labtracker.dto.component.ComponentResponse;
import com.college.labtracker.dto.component.ComponentRestockRequest;
import com.college.labtracker.dto.component.ComponentReturnRequest;
import com.college.labtracker.entity.IssueStatus;
import com.college.labtracker.service.ComponentService;
import jakarta.validation.Valid;

// REST controller exposing electronic component inventory and checkout/return workflows
@RestController
@RequestMapping("/api/components")
public class ComponentController {

    private final ComponentService componentService;

    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    // GET /api/components: List components, optional filter by ?labId=
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ComponentResponse>> getAllComponents(@RequestParam(required = false) Long labId) {
        return ResponseEntity.ok(componentService.getAllComponents(labId));
    }

    // GET /api/components/{id}: Get component stock record
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ComponentResponse> getComponentById(@PathVariable Long id) {
        return ResponseEntity.ok(componentService.getComponentById(id));
    }

    // POST /api/components: Add new component to stock
    @PostMapping
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ComponentResponse> createComponent(@Valid @RequestBody ComponentRequest request) {
        ComponentResponse response = componentService.createComponent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // PUT /api/components/{id}: Update component stock record
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ComponentResponse> updateComponent(
            @PathVariable Long id,
            @Valid @RequestBody ComponentRequest request
    ) {
        return ResponseEntity.ok(componentService.updateComponent(id, request));
    }

    // PUT /api/components/{id}/restock: Add stock to existing component
    @PutMapping("/{id}/restock")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ComponentResponse> restock(
            @PathVariable Long id,
            @Valid @RequestBody ComponentRestockRequest request
    ) {
        return ResponseEntity.ok(componentService.restock(id, request.getQuantityToAdd()));
    }

    // POST /api/components/issue: Checkout components to a student team
    @PostMapping("/issue")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ComponentIssueResponse> issueComponent(@Valid @RequestBody ComponentIssueRequest request) {
        ComponentIssueResponse response = componentService.issueComponent(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST /api/components/issues/{id}/return: Reconcile return of issued components
    @PostMapping("/issues/{id}/return")
    @PreAuthorize("hasAnyRole('LAB_INCHARGE', 'ADMIN')")
    public ResponseEntity<ComponentIssueResponse> returnComponent(
            @PathVariable Long id,
            @Valid @RequestBody ComponentReturnRequest request
    ) {
        return ResponseEntity.ok(componentService.returnComponent(id, request));
    }

    // GET /api/components/issues: View component issuance audit log
    @GetMapping("/issues")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ComponentIssueResponse>> getComponentIssues(
            @RequestParam(required = false) Long teamId,
            @RequestParam(required = false) IssueStatus status,
            @RequestParam(required = false) Long sessionId
    ) {
        return ResponseEntity.ok(componentService.getComponentIssues(teamId, status, sessionId));
    }
}
