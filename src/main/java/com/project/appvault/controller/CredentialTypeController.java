package com.project.appvault.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.appvault.entity.CredentialType;
import com.project.appvault.service.CredentialTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/credentialTypes")
@RequiredArgsConstructor
public class CredentialTypeController {

    private final CredentialTypeService credentialTypeService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public String listCredentialTypes(Model model) {
        model.addAttribute("credentialTypes", credentialTypeService.getAllCredentialTypes());
        return "credentialTypes/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("credentialType", new CredentialType());
        return "credentialTypes/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        CredentialType credentialType = credentialTypeService.getCredentialTypeById(id);
        model.addAttribute("credentialType", credentialType);
        return "credentialTypes/form";
    }

    @PostMapping
    public String saveCredentialType(@Valid @ModelAttribute CredentialType credentialType,
                                     BindingResult bindingResult,
                                     Model model,
                                     RedirectAttributes redirectAttributes,
                                     @RequestParam("schemaJson") String schemaJson) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> schemaList = objectMapper.readValue(schemaJson, new TypeReference<>() {});
            credentialType.setSchema(schemaList);
        } catch (Exception e) {
            bindingResult.rejectValue("schema", "error.credentialType", "Invalid schema format");
        }

        if (credentialTypeService.isNameTakenByOther(credentialType.getName(), credentialType.getId())) {
            bindingResult.rejectValue("name", "error.credentialType", "Name already exists");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("credentialType", credentialType);
            return "credentialTypes/form";
        }

        if (credentialType.getId() != null) {
            credentialTypeService.updateCredentialType(credentialType);
            redirectAttributes.addFlashAttribute("successMessage", "Credential type updated successfully!");
        } else {
            credentialTypeService.saveCredentialType(credentialType);
            redirectAttributes.addFlashAttribute("successMessage", "Credential type created successfully!");
        }

        return "redirect:/credentialTypes";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<CredentialType> getCredentialTypeById(@PathVariable Long id) {
        CredentialType credentialType = credentialTypeService.getCredentialTypeById(id);
        return ResponseEntity.ok(credentialType);
    }

    @PostMapping("/delete/{id}")
    public String deleteCredentialType(@PathVariable Long id,
                                       RedirectAttributes redirectAttributes) {
        credentialTypeService.deleteCredentialType(id);
        redirectAttributes.addFlashAttribute("successMessage", "Credential type deleted successfully.");
        return "redirect:/credentialTypes";
    }

}
