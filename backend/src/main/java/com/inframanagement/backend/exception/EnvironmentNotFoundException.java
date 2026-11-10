package com.inframanagement.backend.exception;

public class EnvironmentNotFoundException extends RuntimeException {

    public EnvironmentNotFoundException(Long environmentId) {
        super("Environment not found with ID: " + environmentId);
    }
}