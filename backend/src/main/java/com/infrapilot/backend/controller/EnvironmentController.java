package com.infrapilot.backend.controller;

import com.infrapilot.backend.model.Environment;
import com.infrapilot.backend.service.EnvironmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/environments")
public class EnvironmentController {

    private final EnvironmentService environmentService;

    public EnvironmentController(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Environment createEnvironment(
            @PathVariable Long projectId,
            @Valid @RequestBody Environment environment) {

        return environmentService.createEnvironment(projectId, environment);
    }

    @GetMapping
    public List<Environment> getEnvironments(
            @PathVariable Long projectId) {

        return environmentService.getEnvironmentsByProject(projectId);
    }

    @GetMapping("/{environmentId}")
    public Environment getEnvironment(
            @PathVariable Long projectId,
            @PathVariable Long environmentId) {

        return environmentService.getEnvironmentById(projectId, environmentId);
    }

    @PutMapping("/{environmentId}")
    public Environment updateEnvironment(
            @PathVariable Long projectId,
            @PathVariable Long environmentId,
            @Valid @RequestBody Environment environment) {

        return environmentService.updateEnvironment(
                projectId, environmentId, environment);
    }

    @DeleteMapping("/{environmentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEnvironment(
            @PathVariable Long projectId,
            @PathVariable Long environmentId) {

        environmentService.deleteEnvironment(projectId, environmentId);
    }
}