package com.uClothes.uClothes.service;

import com.uClothes.uClothes.domain.User;
import com.uClothes.uClothes.dto.ResponseUserDTO;
import com.uClothes.uClothes.repositories.UserRepository;
import com.uClothes.uClothes.security.UserLoginRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        if (this.userRepository.findByUsername(user.getUsername()) != null)
            return new ResponseUserDTO(false, "User with this username already exists.");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        return new ResponseUserDTO(true, "User registered successfully.");
    }

    public ResponseUserDTO loginUser(UserLoginRequest loginRequest, HttpServletResponse response) {
        User user = this.userRepository.findByUsername(loginRequest.getUsername());
        if (user != null && this.passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            String token = this.jwtUtil.generateToken(user);
            user.setCurrentTokenId(token);
            this.userRepository.save(user);
            ResponseCookie cookie = ResponseCookie.from("jwt", token)
                    .httpOnly(true)
                    .secure(true)
                    .domain(".uclothes.pl")
                    .maxAge(3600).path("/")
                    .sameSite("none").build();
            response.setHeader("Set-Cookie", cookie.toString());
            return new ResponseUserDTO(true, user.getRole(), user.getUsername(), token);
        }
        return new ResponseUserDTO(false, "Invalid username or password.");
    }

    public boolean isTokenValid(String token) {
        User user = this.userRepository.findByCurrentTokenId(token);
        return (user != null);
    }

    public ResponseUserDTO logoutUser(String username, HttpServletResponse response) {
        try {
            User user = this.userRepository.findByUsername(username);
            if (user != null) {
                user.setCurrentTokenId(null);
                this.userRepository.save(user);
                String cookieValue = ResponseCookie.from("jwt", "")
                        .httpOnly(true)
                        .secure(true)
                        .domain(".uclothes.pl")
                        .sameSite("none")
                        .maxAge(0)
                        .path("/")
                        .build().toString();
                response.addHeader("Set-Cookie", cookieValue);
                return new ResponseUserDTO(true);
            }
            return new ResponseUserDTO(false, "User not found.");
        } catch (Exception e) {
            return new ResponseUserDTO(false, "Error during logout: " + e.getMessage());
        }
    }

    public ResponseUserDTO getUserByToken(String token) {
        boolean findUser = isTokenValid(token);
        if (!findUser)
            return new ResponseUserDTO(false);
        return new ResponseUserDTO(true);
    }
}
