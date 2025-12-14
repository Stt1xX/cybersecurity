package com.example.lab1.config;

import com.example.lab1.service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {
    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    public void init() {
        if (userService.listUsers().isEmpty()) {
            userService.createUser("user", "password", "USER");
            userService.createUser("admin", "adminpass", "ADMIN");
        }
    }
}

