package com.uClothes.uClothes.bootstrap;

import com.uClothes.uClothes.domain.User;
import com.uClothes.uClothes.domain.UserRole;
import com.uClothes.uClothes.repositories.UserRepository;
import com.uClothes.uClothes.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${ADMIN_NAME}")
    private String adminName;

    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    public AdminInitializer(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if(userRepository.findByUsername(adminName) != null){
        User admin = new User();
        admin.setUsername(adminName);
        admin.setPassword(adminPassword);
        admin.setRole(UserRole.ADMIN);
        userService.registerUser(admin);
        System.out.println("Admin user created.");
        }
        else {
            System.out.println("Admin user is already registered");
        }
    }
}

