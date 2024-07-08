package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.domain.User;
import com.uClothes.uClothes.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/")
    @ResponseBody
    public List<User> login(){
        userRepository.save(new User("Adi", "example", 1));
       List<User> users =   userRepository.findAll();
        System.out.println(users);
        return users;
    }
}
