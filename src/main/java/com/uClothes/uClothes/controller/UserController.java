package com.uClothes.uClothes.controller;

import com.uClothes.uClothes.dto.ResponseUserDTO;
import com.uClothes.uClothes.security.UserLoginRequest;
import com.uClothes.uClothes.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUserByToken/{token}")
    @ResponseBody
    public ResponseUserDTO getUserByToken(@PathVariable String token) {
        return userService.getUserByToken(token);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseUserDTO loginUser(@RequestBody UserLoginRequest loginRequest, HttpServletResponse response) {
        return userService.loginUser(loginRequest, response);
    }


    @GetMapping("/logout")
    @ResponseBody
    public ResponseUserDTO logoutUser(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getUserPrincipal().getName();
        return userService.logoutUser(username, response);
    }
}
