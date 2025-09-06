package com.project.appvault.controller;

import com.project.appvault.entity.Project;
import com.project.appvault.service.ClientService;
import com.project.appvault.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final ClientService clientService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        return "projects/list";
    }

    @PostMapping
    public String saveProject(@Valid @ModelAttribute Project project,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (projectService.isProjectNameTakenByClient(project.getName(), project.getClient(), project.getId())) {
            bindingResult.rejectValue("name", "error.project", "Project with this name already exists for the selected client");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("clients", clientService.getAllClients());
            return "projects/form";
        }

        if (project.getId() != null) {
            projectService.updateProject(project);
            redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully!");
        } else {
            projectService.saveProject(project);
            redirectAttributes.addFlashAttribute("successMessage", "Project created successfully!");
        }

        return "redirect:/projects";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.deleteProject(id);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully.");
        return "redirect:/projects";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("clients", clientService.getAllClients());
        return "projects/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Project project = projectService.getProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("clients", clientService.getAllClients());
        return "projects/form";
    }
}
