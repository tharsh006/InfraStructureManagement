package com.inframanagement.backend.service;

import com.inframanagement.backend.dto.EnvironmentResponse;
import com.inframanagement.backend.exception.EnvironmentNotFoundException;
import com.inframanagement.backend.exception.ProjectNotFoundException;
import com.inframanagement.backend.model.Environment;
import com.inframanagement.backend.model.Project;
import com.inframanagement.backend.repository.EnvironmentRepository;
import com.inframanagement.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnvironmentService {

    private final EnvironmentRepository environmentRepository;
    private final ProjectRepository projectRepository;

    public EnvironmentService(
            EnvironmentRepository environmentRepository,
            ProjectRepository projectRepository) {
        this.environmentRepository = environmentRepository;
        this.projectRepository = projectRepository;
    }

    public EnvironmentResponse createEnvironment(
            Long projectId, Environment environment) {

        Project project = getProject(projectId);

        if (environment.getType() == null) {
            throw new IllegalArgumentException(
                    "Environment type is required");
        }

        if (environmentRepository.existsByProjectIdAndNameIgnoreCase(
                projectId, environment.getName())) {
            throw new IllegalArgumentException(
                    "An environment with this name already exists in this project");
        }

        environment.setProject(project);

        Environment savedEnvironment =
                environmentRepository.save(environment);

        return toResponse(savedEnvironment);
    }

    public List<EnvironmentResponse> getEnvironmentsByProject(
            Long projectId) {

        getProject(projectId);

        return environmentRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EnvironmentResponse getEnvironmentById(
            Long projectId, Long environmentId) {

        getProject(projectId);

        Environment environment =
                findEnvironment(projectId, environmentId);

        return toResponse(environment);
    }

    public EnvironmentResponse updateEnvironment(
            Long projectId,
            Long environmentId,
            Environment updatedEnvironment) {

        getProject(projectId);

        Environment existingEnvironment =
                findEnvironment(projectId, environmentId);

        if (updatedEnvironment.getType() == null) {
            throw new IllegalArgumentException(
                    "Environment type is required");
        }

        if (environmentRepository
                .existsByProjectIdAndNameIgnoreCaseAndIdNot(
                        projectId,
                        updatedEnvironment.getName(),
                        environmentId)) {
            throw new IllegalArgumentException(
                    "An environment with this name already exists in this project");
        }

        existingEnvironment.setName(updatedEnvironment.getName());
        existingEnvironment.setDescription(
                updatedEnvironment.getDescription());
        existingEnvironment.setType(updatedEnvironment.getType());

        Environment savedEnvironment =
                environmentRepository.save(existingEnvironment);

        return toResponse(savedEnvironment);
    }

    public void deleteEnvironment(
            Long projectId, Long environmentId) {

        getProject(projectId);

        Environment environment =
                findEnvironment(projectId, environmentId);

        environmentRepository.delete(environment);
    }

    private Project getProject(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(projectId));
    }

    private Environment findEnvironment(
            Long projectId, Long environmentId) {

        return environmentRepository
                .findByIdAndProjectId(environmentId, projectId)
                .orElseThrow(() ->
                        new EnvironmentNotFoundException(environmentId));
    }

    private EnvironmentResponse toResponse(Environment environment) {
        return new EnvironmentResponse(
                environment.getId(),
                environment.getName(),
                environment.getDescription(),
                environment.getType(),
                environment.getProject().getId(),
                environment.getCreatedAt()
        );
    }
}