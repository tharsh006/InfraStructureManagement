package com.infrapilot.backend.service;

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

    public Environment createEnvironment(Long projectId, Environment environment) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException(
                        "Project not found with ID: " + projectId));

        environment.setProject(project);

        return environmentRepository.save(environment);
    }

    public List<Environment> getEnvironmentsByProject(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new RuntimeException(
                    "Project not found with ID: " + projectId);
        }

        return environmentRepository.findByProjectId(projectId);
    }

    public Environment getEnvironmentById(Long projectId, Long environmentId) {

        return environmentRepository
                .findByIdAndProjectId(environmentId, projectId)
                .orElseThrow(() -> new RuntimeException(
                        "Environment not found with ID: " + environmentId));
    }

    public Environment updateEnvironment(
            Long projectId, Long environmentId, Environment updatedEnvironment) {

        Environment existingEnvironment =
                getEnvironmentById(projectId, environmentId);

        existingEnvironment.setName(updatedEnvironment.getName());
        existingEnvironment.setDescription(updatedEnvironment.getDescription());

        return environmentRepository.save(existingEnvironment);
    }

    public void deleteEnvironment(Long projectId, Long environmentId) {

        Environment environment =
                getEnvironmentById(projectId, environmentId);

        environmentRepository.delete(environment);
    }
}