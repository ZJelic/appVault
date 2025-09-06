package com.project.appvault.repository;

import com.project.appvault.entity.Project;
import com.project.appvault.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByNameAndClientAndIdNot(String name, Client client, Long id);
}
