package com.project.appvault.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.appvault.entity.Credential;
import com.project.appvault.service.CredentialService;
import com.project.appvault.service.CredentialTypeService;
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

import java.util.Map;

@Controller
@RequestMapping("/credentials")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;
    private final ProjectService projectService;
    private final CredentialTypeService credentialTypeService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String listCredentials(Model model) {
        model.addAttribute("credentials", credentialService.getAllCredentials());
        return "credentials/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("credential", new Credential());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("credentialTypes", credentialTypeService.getAllCredentialTypes());
        return "credentials/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Credential credential = credentialService.getCredentialById(id);
        model.addAttribute("credential", credential);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("credentialTypes", credentialTypeService.getAllCredentialTypes());
        return "credentials/form";
    }

    @PostMapping
    public String saveCredential(@Valid @ModelAttribute Credential credential,
                                 BindingResult bindingResult,
                                 Model model,
                                 RedirectAttributes redirectAttributes,
                                 @RequestParam("dataJson") String dataJson) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> dataMap = objectMapper.readValue(dataJson, new TypeReference<>() {});
            credential.setData(dataMap);
        } catch (Exception e) {
            bindingResult.rejectValue("data", "error.credential", "Invalid data format");
        }

        if (credentialService.isNameTakenInProject(
                credential.getName(),
                credential.getProject().getId(),
                credential.getId()
        )) {
            bindingResult.rejectValue("name", "error.credential", "Name already exists in this project");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("credential", credential);
            model.addAttribute("projects", projectService.getAllProjects());
            model.addAttribute("credentialTypes", credentialTypeService.getAllCredentialTypes());
            return "credentials/form";
        }

        if (credential.getId() != null) {
            credentialService.updateCredential(credential);
            redirectAttributes.addFlashAttribute("successMessage", "Credential updated successfully!");
        } else {
            credentialService.saveCredential(credential);
            redirectAttributes.addFlashAttribute("successMessage", "Credential created successfully!");
        }

        return "redirect:/credentials";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<Credential> getCredentialById(@PathVariable Long id) {
        Credential credential = credentialService.getCredentialById(id);
        return ResponseEntity.ok(credential);
    }

    @PostMapping("/delete/{id}")
    public String deleteCredential(@PathVariable Long id,
                                   RedirectAttributes redirectAttributes) {
        credentialService.deleteCredential(id);
        redirectAttributes.addFlashAttribute("successMessage", "Credential deleted successfully.");
        return "redirect:/credentials";
    }
}
