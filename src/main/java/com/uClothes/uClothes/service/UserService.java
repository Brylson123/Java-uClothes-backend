package com.uClothes.uClothes.service;

import com.uClothes.uClothes.domain.User;
import com.uClothes.uClothes.dto.ResponseUserDTO;
import com.uClothes.uClothes.repositories.UserRepository;
import com.uClothes.uClothes.security.UserLoginRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtilService jwtUtil;

    public UserService(JwtUtilService jwtUtil, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public ResponseUserDTO registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return new ResponseUserDTO(false, "User with this username already exists.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseUserDTO(true, "User registered successfully.");
    }

    public ResponseUserDTO loginUser(UserLoginRequest loginRequest, HttpServletResponse response) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = jwtUtil.generateToken(user);
            user.setCurrentTokenId(token);
            userRepository.save(user);
            Cookie cookie = new Cookie("jwt", token);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(10 * 60 * 60);
            response.addCookie(cookie);
            return new ResponseUserDTO(true, token);
        }
        return new ResponseUserDTO(false, "Invalid username or password.");
    }

    public boolean isTokenValid(String token) {
        User user = userRepository.findByCurrentTokenId(token);
        return user != null;
    }

    public ResponseUserDTO logoutUser(String username, HttpServletResponse response) {
        try {
            User user = userRepository.findByUsername(username);
            if (user != null) {
                user.setCurrentTokenId(null);
                userRepository.save(user);
                Cookie cookie = new Cookie("jwt", "");
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                return new ResponseUserDTO(true, "User logged out successfully.");
            } else {
                return new ResponseUserDTO(false, "User not found.");
            }
        } catch (Exception e) {
            return new ResponseUserDTO(false, "Error during logout: " + e.getMessage());
        }
    }
}
