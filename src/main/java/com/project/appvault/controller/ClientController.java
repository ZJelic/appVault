package com.project.appvault.controller;

import com.project.appvault.entity.Client;
import com.project.appvault.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAllClients());
        return "clients/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }

    @PostMapping("/save")
    public String saveClient(@Valid @ModelAttribute("client") Client client,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {

        if (clientService.isNameTakenByOther(client.getName(), client.getId())) {
            result.rejectValue("name", "error.client", "Name is already taken.");
        }

        if (result.hasErrors()) {
            return "clients/form";
        }

        if (client.getId() != null) {
            clientService.updateClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Client updated successfully.");
        } else {
            clientService.saveClient(client);
            redirectAttributes.addFlashAttribute("successMessage", "Client created successfully.");
        }

        return "redirect:/clients";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Client client = clientService.getClientById(id);
        if (client == null) {
            return "redirect:/clients";
        }

        model.addAttribute("client", client);
        return "clients/form";
    }

    @PostMapping("/delete/{id}")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clientService.deleteClient(id);
        redirectAttributes.addFlashAttribute("successMessage", "Client deleted successfully.");
        return "redirect:/clients";
    }
}
