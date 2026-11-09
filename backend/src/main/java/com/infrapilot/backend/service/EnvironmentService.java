package com.infrapilot.backend.service;

import com.infrapilot.backend.dto.EnvironmentResponse;
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

    public EnvironmentResponse createEnvironment(
            Long projectId, Environment environment) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(projectId));

        environment.setProject(project);

        Environment saved = environmentRepository.save(environment);

        return toResponse(saved);
    }

    public List<EnvironmentResponse> getEnvironmentsByProject(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }

        return environmentRepository.findByProjectId(projectId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EnvironmentResponse getEnvironmentById(
            Long projectId, Long environmentId) {

        checkProjectExists(projectId);

        Environment environment = findEnvironment(projectId, environmentId);

        return toResponse(environment);
    }

    public EnvironmentResponse updateEnvironment(
            Long projectId,
            Long environmentId,
            Environment updatedEnvironment) {

        checkProjectExists(projectId);

        Environment existingEnvironment = findEnvironment(projectId, environmentId);

        existingEnvironment.setName(updatedEnvironment.getName());
        existingEnvironment.setDescription(updatedEnvironment.getDescription());

        Environment saved = environmentRepository.save(existingEnvironment);

        return toResponse(saved);
    }

    public void deleteEnvironment(Long projectId, Long environmentId) {

        checkProjectExists(projectId);

        Environment environment = findEnvironment(projectId, environmentId);

        environmentRepository.delete(environment);
    }

    private void checkProjectExists(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectNotFoundException(projectId);
        }
    }

    private Environment findEnvironment(Long projectId, Long environmentId) {
        return environmentRepository
                .findByIdAndProjectId(environmentId, projectId)
                .orElseThrow(() -> new EnvironmentNotFoundException(environmentId));
    }

    private EnvironmentResponse toResponse(Environment environment) {
        return new EnvironmentResponse(
                environment.getId(),
                environment.getName(),
                environment.getDescription(),
                environment.getProject().getId(),
                environment.getCreatedAt()
        );
    }
}