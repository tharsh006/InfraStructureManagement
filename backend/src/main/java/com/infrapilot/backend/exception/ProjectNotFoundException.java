package com.infrapilot.backend.exception;

public class ProjectNotFoundException extends RuntimeException {

    public ProjectNotFoundException(Long id) {
        super("Project not found with ID: " + id);
    }
}