package com.infrapilot.backend.service;
import com.infrapilot.backend.exception.ProjectNotFoundException;

import com.infrapilot.backend.model.Project;
import com.infrapilot.backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Create a project
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    // Get all projects
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    // Get project by ID
    public Project getProjectById(Long id) {
    return projectRepository.findById(id)
            .orElseThrow(() -> new ProjectNotFoundException(id));
}

    // Delete project
    public void deleteProject(Long id) {
    Project project = getProjectById(id);
    projectRepository.delete(project);
}

	// Update an existing project
public Project updateProject(Long id, Project updatedProject) {

    Project existingProject = getProjectById(id);

    existingProject.setName(updatedProject.getName());
    existingProject.setDescription(updatedProject.getDescription());

    return projectRepository.save(existingProject);
}

}