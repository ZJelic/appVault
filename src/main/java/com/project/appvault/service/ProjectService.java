package com.project.appvault.service;

import com.project.appvault.entity.Project;
import com.project.appvault.entity.Client;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects();
    Project getProjectById(Long id);
    void saveProject(Project project);
    void updateProject(Project project);
    void deleteProject(Long id);
    boolean isProjectNameTakenByClient(String name, Client client, Long projectId);
}
