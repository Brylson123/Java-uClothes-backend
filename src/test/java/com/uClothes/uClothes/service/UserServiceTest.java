package com.uClothes.uClothes.service;

import com.uClothes.uClothes.domain.User;
import com.uClothes.uClothes.domain.UserRole;
import com.uClothes.uClothes.dto.ResponseUserDTO;
import com.uClothes.uClothes.repositories.UserRepository;
import com.uClothes.uClothes.security.UserLoginRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtilService jwtUtil;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private UserService userService;
    private AutoCloseable closeable;
    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        ResponseUserDTO result = userService.registerUser(user);

        assertTrue(result.isSuccess());
        assertEquals("User registered successfully.", result.getError());
        verify(userRepository).save(user);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(new User());

        ResponseUserDTO result = userService.registerUser(user);

        assertFalse(result.isSuccess());
        assertEquals("User with this username already exists.", result.getError());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");
        user.setRole(UserRole.ADMIN);

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken(user)).thenReturn("jwtToken");

        ResponseUserDTO result = userService.loginUser(loginRequest, response);

        assertTrue(result.isSuccess());
        assertEquals(UserRole.ADMIN, result.getUserRole());
        assertEquals("testUser", result.getUser());
        assertEquals("jwtToken", result.getJwtToken());
        verify(userRepository).save(user);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void testLoginUser_InvalidCredentials() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("encodedPassword");

        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("wrongPassword");

        when(userRepository.findByUsername("testUser")).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        ResponseUserDTO result = userService.loginUser(loginRequest, response);

        assertFalse(result.isSuccess());
        assertEquals("Invalid username or password.", result.getError());
        verify(response, never()).addCookie(any(Cookie.class));
    }

    @Test
    void testIsTokenValid_ValidToken() {
        User user = new User();
        user.setCurrentTokenId("validToken");

        when(userRepository.findByCurrentTokenId("validToken")).thenReturn(user);

        assertTrue(userService.isTokenValid("validToken"));
    }

    @Test
    void testIsTokenValid_InvalidToken() {
        when(userRepository.findByCurrentTokenId("invalidToken")).thenReturn(null);

        assertFalse(userService.isTokenValid("invalidToken"));
    }

    @Test
    void testLogoutUser_Success() {
        User user = new User();
        user.setUsername("testUser");

        when(userRepository.findByUsername("testUser")).thenReturn(user);

        ResponseUserDTO result = userService.logoutUser("testUser", response);

        assertTrue(result.isSuccess());
        verify(userRepository).save(user);
        verify(response).addCookie(any(Cookie.class));
    }

    @Test
    void testLogoutUser_UserNotFound() {
        when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        ResponseUserDTO result = userService.logoutUser("unknownUser", response);

        assertFalse(result.isSuccess());
        assertEquals("User not found.", result.getError());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByToken_ValidToken() {
        when(userRepository.findByCurrentTokenId("validToken")).thenReturn(new User());

        ResponseUserDTO result = userService.getUserByToken("validToken");

        assertTrue(result.isSuccess());
    }

    @Test
    void testGetUserByToken_InvalidToken() {
        when(userRepository.findByCurrentTokenId("invalidToken")).thenReturn(null);

        ResponseUserDTO result = userService.getUserByToken("invalidToken");

        assertFalse(result.isSuccess());
    }
}
