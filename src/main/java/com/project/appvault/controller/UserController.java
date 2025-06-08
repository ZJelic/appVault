package com.project.appvault.controller;

import com.project.appvault.entity.User;
import com.project.appvault.service.RoleService;
import com.project.appvault.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    @PostMapping
    public String saveUser(@Valid @ModelAttribute User user,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        if (userService.isUsernameTakenByOther(user.getUsername(), user.getId())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
        }

        if (userService.isEmailTakenByOther(user.getEmail(), user.getId())) {
            bindingResult.rejectValue("email", "error.user", "Email already exists");
        }

        if (user.getId() == null) {
            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                bindingResult.rejectValue("password", "error.user", "Password is required");
            } else if (user.getPassword().length() < 6) {
                bindingResult.rejectValue("password", "error.user", "Password must be at least 6 characters");
            }
        } else if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            if (user.getPassword().length() < 6) {
                bindingResult.rejectValue("password", "error.user", "Password must be at least 6 characters");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/form";
        }

        if (user.getId() != null) {
            userService.updateUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
        } else {
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "User created successfully!");
        }

        return "redirect:/users";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully.");
        return "redirect:/users";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("usernameExistsError", null);
        return "users/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "users/form";
    }
}
