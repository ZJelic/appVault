package com.project.appvault.controller;

import com.project.appvault.entity.User;
import com.project.appvault.exception.EmailAlreadyExistsException;
import com.project.appvault.exception.UsernameAlreadyExistsException;
import com.project.appvault.service.RoleService;
import com.project.appvault.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String saveUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.getAllRoles());
            return "users/form";
        }
        try {
            if (user.getId() != null) {
                userService.updateUser(user);
            } else {
                userService.saveUser(user);
            }
            return "redirect:/users";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("usernameExistsError", e.getMessage());
            return "users/form";
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("emailExistsError", e.getMessage());
            return "users/form";
        }
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
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
