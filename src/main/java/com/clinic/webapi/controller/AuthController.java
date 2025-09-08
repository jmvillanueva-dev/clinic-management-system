package com.clinic.webapi.controller;

import com.clinic.webapi.dto.AuthRequest;
import com.clinic.webapi.dto.AuthResponse;
import com.clinic.webapi.dto.RegisterRequest;
import com.clinic.webapi.model.entity.User;
import com.clinic.webapi.security.JwtService;
import com.clinic.webapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        Set<String> roles = user.getRoles().stream()
                .map(r -> r.getName())
                .collect(Collectors.toSet());

        String token = jwtService.generateToken(user.getId(), user.getEmail(), roles);

        return new AuthResponse(token, "Bearer");
    }
}

