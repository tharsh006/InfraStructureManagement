package com.infrapilot.backend.repository;

import com.infrapilot.backend.model.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnvironmentRepository extends JpaRepository<Environment, Long> {

    List<Environment> findByProjectId(Long projectId);

    Optional<Environment> findByIdAndProjectId(Long id, Long projectId);
}