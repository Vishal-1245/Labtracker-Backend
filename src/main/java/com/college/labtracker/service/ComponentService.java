package com.college.labtracker.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.college.labtracker.dto.component.ComponentIssueRequest;
import com.college.labtracker.dto.component.ComponentIssueResponse;
import com.college.labtracker.dto.component.ComponentRequest;
import com.college.labtracker.dto.component.ComponentResponse;
import com.college.labtracker.dto.component.ComponentReturnRequest;
import com.college.labtracker.entity.Component;
import com.college.labtracker.entity.ComponentIssue;
import com.college.labtracker.entity.IssueStatus;
import com.college.labtracker.entity.Lab;
import com.college.labtracker.entity.Team;
import com.college.labtracker.exception.InsufficientStockException;
import com.college.labtracker.exception.ResourceNotFoundException;
import com.college.labtracker.repository.ComponentIssueRepository;
import com.college.labtracker.repository.ComponentRepository;
import com.college.labtracker.repository.LabRepository;
import com.college.labtracker.repository.TeamRepository;

// Service managing component stock, allocations to lab teams, and return/damage reconciliation
@Service
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final LabRepository labRepository;
    private final ComponentIssueRepository issueRepository;
    private final TeamRepository teamRepository;

    public ComponentService(
            ComponentRepository componentRepository,
            LabRepository labRepository,
            ComponentIssueRepository issueRepository,
            TeamRepository teamRepository
    ) {
        this.componentRepository = componentRepository;
        this.labRepository = labRepository;
        this.issueRepository = issueRepository;
        this.teamRepository = teamRepository;
    }

    public ComponentResponse toResponse(Component c) {
        return new ComponentResponse(
                c.getId(),
                c.getLab().getId(),
                c.getLab().getName(),
                c.getName(),
                c.getCategory(),
                c.getTotalQuantity(),
                c.getAvailableQuantity()
        );
    }

    public ComponentIssueResponse toIssueResponse(ComponentIssue issue) {
        return new ComponentIssueResponse(
                issue.getId(),
                issue.getTeam().getId(),
                issue.getTeam().getTeamName(),
                issue.getComponent().getId(),
                issue.getComponent().getName(),
                issue.getQuantityIssued(),
                issue.getQuantityReturned(),
                issue.getQuantityDamaged(),
                issue.getIssuedAt(),
                issue.getReturnedAt(),
                issue.getStatus()
        );
    }

    // List components, optionally filtered by lab
    public List<ComponentResponse> getAllComponents(Long labId) {
        List<Component> list = (labId != null)
                ? componentRepository.findByLabId(labId)
                : componentRepository.findAll();
        return list.stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Get component by ID
    public ComponentResponse getComponentById(Long id) {
        Component c = componentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Component not found with ID: " + id));
        return toResponse(c);
    }

    // Register a new component in inventory
    @Transactional
    public ComponentResponse createComponent(ComponentRequest request) {
        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));

        Component component = new Component(
                lab,
                request.getName(),
                request.getCategory(),
                request.getTotalQuantity(),
                request.getAvailableQuantity()
        );
        Component saved = componentRepository.save(component);
        return toResponse(saved);
    }

    // Update component details
    @Transactional
    public ComponentResponse updateComponent(Long id, ComponentRequest request) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Component not found with ID: " + id));

        if (!component.getLab().getId().equals(request.getLabId())) {
            Lab newLab = labRepository.findById(request.getLabId())
                    .orElseThrow(() -> new ResourceNotFoundException("Lab not found with ID: " + request.getLabId()));
            component.setLab(newLab);
        }

        component.setName(request.getName());
        component.setCategory(request.getCategory());
        component.setTotalQuantity(request.getTotalQuantity());
        component.setAvailableQuantity(request.getAvailableQuantity());

        Component updated = componentRepository.save(component);
        return toResponse(updated);
    }

    // Restock component quantity
    @Transactional
    public ComponentResponse restock(Long id, int quantityToAdd) {
        Component component = componentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Component not found with ID: " + id));

        component.setTotalQuantity(component.getTotalQuantity() + quantityToAdd);
        component.setAvailableQuantity(component.getAvailableQuantity() + quantityToAdd);

        Component updated = componentRepository.save(component);
        return toResponse(updated);
    }

    // Issue components to a student team for a lab session
    @Transactional
    public ComponentIssueResponse issueComponent(ComponentIssueRequest request) {
        Team team = teamRepository.findById(request.getTeamId())
                .orElseThrow(() -> new ResourceNotFoundException("Team not found with ID: " + request.getTeamId()));

        Component component = componentRepository.findById(request.getComponentId())
                .orElseThrow(() -> new ResourceNotFoundException("Component not found with ID: " + request.getComponentId()));

        // Check stock availability
        if (component.getAvailableQuantity() < request.getQuantity()) {
            throw new InsufficientStockException("Insufficient stock for component '" + component.getName() 
                    + "'. Available: " + component.getAvailableQuantity() + ", Requested: " + request.getQuantity());
        }

        // Deduct from available inventory
        component.setAvailableQuantity(component.getAvailableQuantity() - request.getQuantity());
        componentRepository.save(component);

        ComponentIssue issue = new ComponentIssue(
                team,
                component,
                request.getQuantity(),
                0,
                0,
                LocalDateTime.now(),
                null,
                IssueStatus.ISSUED
        );
        ComponentIssue savedIssue = issueRepository.save(issue);
        return toIssueResponse(savedIssue);
    }

    // Reconcile return of components with recorded damages
    @Transactional
    public ComponentIssueResponse returnComponent(Long issueId, ComponentReturnRequest request) {
        ComponentIssue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("Component issue record not found with ID: " + issueId));

        if (issue.getStatus() == IssueStatus.RETURNED || issue.getStatus() == IssueStatus.RETURNED_WITH_ISSUES) {
            throw new IllegalArgumentException("Components for this issue record have already been processed");
        }

        int totalAccounted = request.getQuantityReturned() + request.getQuantityDamaged();
        if (totalAccounted != issue.getQuantityIssued()) {
            throw new IllegalArgumentException("Total returned (" + request.getQuantityReturned() 
                    + ") and damaged (" + request.getQuantityDamaged() 
                    + ") must equal issued quantity (" + issue.getQuantityIssued() + ")");
        }

        Component component = issue.getComponent();
        // Return good components to available stock
        component.setAvailableQuantity(component.getAvailableQuantity() + request.getQuantityReturned());
        // Reduce damaged items from total inventory
        component.setTotalQuantity(component.getTotalQuantity() - request.getQuantityDamaged());
        componentRepository.save(component);

        issue.setQuantityReturned(request.getQuantityReturned());
        issue.setQuantityDamaged(request.getQuantityDamaged());
        issue.setReturnedAt(LocalDateTime.now());
        issue.setStatus(request.getQuantityDamaged() > 0 ? IssueStatus.RETURNED_WITH_ISSUES : IssueStatus.RETURNED);

        ComponentIssue updatedIssue = issueRepository.save(issue);
        return toIssueResponse(updatedIssue);
    }

    // List component issues with optional filters
    public List<ComponentIssueResponse> getComponentIssues(Long teamId, IssueStatus status, Long sessionId) {
        List<ComponentIssue> list;
        if (teamId != null) {
            list = issueRepository.findByTeamId(teamId);
        } else if (sessionId != null) {
            list = issueRepository.findByTeamSessionId(sessionId);
        } else if (status != null) {
            list = issueRepository.findByStatus(status);
        } else {
            list = issueRepository.findAll();
        }

        return list.stream().map(this::toIssueResponse).collect(Collectors.toList());
    }
}
