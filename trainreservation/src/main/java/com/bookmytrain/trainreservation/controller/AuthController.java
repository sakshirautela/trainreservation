package com.bookmytrain.trainreservation.controller;

import com.bookmytrain.trainreservation.dto.AuthRequest;
import com.bookmytrain.trainreservation.dto.AuthResponse;
import com.bookmytrain.trainreservation.entity.UserEntity;
import com.bookmytrain.trainreservation.security.JwtService;
import com.bookmytrain.trainreservation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        UserEntity user = userService.createUser(request);
        String token = jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken((UserEntity) authentication.getPrincipal());
            return ResponseEntity.ok(new AuthResponse(token, "Login successful"));
        }
        throw new RuntimeException("Invalid credentials");
    }
}