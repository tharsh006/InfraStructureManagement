
package com.infrapilot.backend.service;

import com.infrapilot.backend.dto.EnvironmentResponse;
import com.infrapilot.backend.enums.EnvironmentType;
import com.infrapilot.backend.exception.EnvironmentNotFoundException;
import com.infrapilot.backend.exception.ProjectNotFoundException;
import com.infrapilot.backend.model.Environment;
import com.infrapilot.backend.model.Project;
import com.infrapilot.backend.repository.EnvironmentRepository;
import com.infrapilot.backend.repository.ProjectRepository;
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

    // Create environment
    public EnvironmentResponse createEnvironment(
            Long projectId, Environment environment) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(projectId));

        if (environment.getType() == null) {
            throw new IllegalArgumentException(
                    "Environment type is required");
        }

        environment.setProject(project);

        Environment saved = environmentRepository.save(environment);

        return toResponse(saved);
    }

    // Get all environments belonging to a project
    public List<EnvironmentResponse> getEnvironmentsByProject(
            Long projectId) {

        checkProjectExists(projectId);

        return environmentRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // Get one environment
    public EnvironmentResponse getEnvironmentById(
            Long projectId, Long environmentId) {

        checkProjectExists(projectId);

        Environment environment =
                findEnvironment(projectId, environmentId);

        return toResponse(environment);
    }

    // Update environment
    public EnvironmentResponse updateEnvironment(
            Long projectId,
            Long environmentId,
            Environment updatedEnvironment) {

        checkProjectExists(projectId);

        Environment existingEnvironment =
                findEnvironment(projectId, environmentId);

        if (updatedEnvironment.getType() == null) {
            throw new IllegalArgumentException(
                    "Environment type is required");
        }

        existingEnvironment.setName(updatedEnvironment.getName());
        existingEnvironment.setDescription(
                updatedEnvironment.getDescription());
        existingEnvironment.setType(updatedEnvironment.getType());

        Environment saved =
                environmentRepository.save(existingEnvironment);

        return toResponse(saved);
    }

    // Delete environment
    public void deleteEnvironment(
            Long projectId, Long environmentId) {

        checkProjectExists(projectId);

        Environment environment =
                findEnvironment(projectId, environmentId);

        environmentRepository.delete(environment);
    }

    // Check whether project exists
    private void checkProjectExists(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
    }

    // Find environment under the specified project
    private Environment findEnvironment(
            Long projectId, Long environmentId) {

        return environmentRepository
                .findByIdAndProjectId(environmentId, projectId)
                .orElseThrow(() ->
                        new EnvironmentNotFoundException(environmentId));
    }

    // Convert entity into DTO
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
