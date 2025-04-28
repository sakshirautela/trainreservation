package com.bookmytrain.trainreservation.service;

import com.train.reservation.dto.AuthRequest;
import com.train.reservation.entity.UserEntity;
import com.train.reservation.exception.DuplicateResourceException;
import com.train.reservation.exception.ResourceNotFoundException;
import com.train.reservation.repository.UserRepository;
import com.train.reservation.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Register a new user
     */
    @Transactional
    public UserEntity registerUser(AuthRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use");
        }

        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setRoles("ROLE_USER"); // Default role

        return userRepository.save(user);
    }

    /**
     * Authenticate user and generate JWT token
     */
    public String authenticateUser(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        UserEntity user = (UserEntity) authentication.getPrincipal();
        return jwtService.generateToken(user);
    }

    /**
     * Get all users (admin only)
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user by ID
     */
    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    /**
     * Get user by email
     */
    public UserEntity getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    /**
     * Update user profile
     */
    @Transactional
    public UserEntity updateUser(Long id, UserEntity updatedUser) {
        UserEntity user = getUserById(id);

        // Update allowed fields
        user.setFirstName(updatedUser.getFirstName());
        user.setLastName(updatedUser.getLastName());
        user.setPhone(updatedUser.getPhone());

        return userRepository.save(user);
    }

    /**
     * Delete user account
     */
    @Transactional
    public void deleteUser(Long id) {
        UserEntity user = getUserById(id);
        userRepository.delete(user);
    }

    /**
     * Change user password
     */
    @Transactional
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        UserEntity user = getUserById(userId);

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // Update to new password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * Upgrade user to admin role
     */
    @Transactional
    public UserEntity upgradeToAdmin(Long id) {
        UserEntity user = getUserById(id);
        if (!user.getRoles().contains("ROLE_ADMIN")) {
            user.setRoles("ROLE_ADMIN,ROLE_USER");
            return userRepository.save(user);
        }
        return user;
    }
}