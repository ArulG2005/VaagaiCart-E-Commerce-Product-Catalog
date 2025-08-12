package com.vaagaicart.backend.service;

import com.vaagaicart.backend.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    // Auth & Registration
    ResponseEntity<?> registerUser(UserRegistrationDto registrationDto, MultipartFile avatar);
    ResponseEntity<?> authenticateUser(LoginDto loginDto);
    ResponseEntity<?> logout();

    // Password Management
    ResponseEntity<?> forgotPassword(String email);
    ResponseEntity<?> resetPassword(String token, PasswordResetDto passwordResetDto);
    ResponseEntity<?> changePassword(PasswordChangeDto passwordChangeDto);

    // Profile
    ResponseEntity<?> getUserProfile();
    ResponseEntity<?> updateProfile(UserUpdateDto updateDto, MultipartFile avatar);

    // Admin
    ResponseEntity<?> getAllUsers();
    ResponseEntity<?> getUserById(Long id);
    ResponseEntity<?> updateUser(Long id, AdminUserUpdateDto updateDto);
    ResponseEntity<?> deleteUser(Long id);
}
