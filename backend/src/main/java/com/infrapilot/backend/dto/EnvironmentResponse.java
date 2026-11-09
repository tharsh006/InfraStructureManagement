package com.infrapilot.backend.dto;

import java.time.LocalDateTime;

public class EnvironmentResponse {

    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private LocalDateTime createdAt;

    public EnvironmentResponse(
            Long id,
            String name,
            String description,
            Long projectId,
            LocalDateTime createdAt) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.projectId = projectId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Long getProjectId() {
        return projectId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}