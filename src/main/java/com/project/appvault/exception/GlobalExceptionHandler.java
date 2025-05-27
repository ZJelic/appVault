package com.project.appvault.exception;

import com.project.appvault.entity.User;
import com.project.appvault.service.RoleService;
import com.project.appvault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private RoleService roleService;

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUsernameExists(UsernameAlreadyExistsException e, Model model) {
        model.addAttribute("usernameExistsError", e.getMessage());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "users/form";
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public String handleEmailExists(EmailAlreadyExistsException e, Model model) {
        model.addAttribute("emailExistsError", e.getMessage());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("user", new User());
        return "users/form";
    }
}
