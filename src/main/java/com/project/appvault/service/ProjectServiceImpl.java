package com.project.appvault.service;

import com.project.appvault.entity.Project;
import com.project.appvault.entity.Client;
import com.project.appvault.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found with id " + id));
    }

    @Override
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void updateProject(Project project) {
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public boolean isProjectNameTakenByClient(String name, Client client, Long projectId) {
        Long excludeId = projectId == null ? -1 : projectId;
        return projectRepository.existsByNameAndClientAndIdNot(name, client, excludeId);
    }
}
