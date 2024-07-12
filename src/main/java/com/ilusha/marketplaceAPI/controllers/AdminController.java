package com.ilusha.marketplaceAPI.controllers;

import com.ilusha.marketplaceAPI.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;


    @GetMapping("/adminuser")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public String hiUserorAdmin() {
        return "Your role is user or admin";
    }

    @GetMapping("/none")
    public String hi() {
        System.out.println(userService.getCurrentUser().getRole());
        return "none";
    }
}
