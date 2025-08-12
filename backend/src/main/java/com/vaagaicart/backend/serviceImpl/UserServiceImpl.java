package com.vaagaicart.backend.serviceImpl;

import com.vaagaicart.backend.controller.OrderController;
import com.vaagaicart.backend.dto.*;
import com.vaagaicart.backend.entity.User;
import com.vaagaicart.backend.exception.CustomException;
import com.vaagaicart.backend.repository.UserRepository;
import com.vaagaicart.backend.service.UserService;
import com.vaagaicart.backend.util.EmailService;
import com.vaagaicart.backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final OrderController orderController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${app.base-url}")
    private String baseUrl;

    UserServiceImpl(OrderController orderController) {
        this.orderController = orderController;
    }

    @Override
    public ResponseEntity<?> registerUser(UserRegistrationDto registrationDto, MultipartFile avatar) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Email already exists"));
        }

        User user = new User();
        user.setName(registrationDto.getName());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        user.setRole("USER");

        if (avatar != null && !avatar.isEmpty()) {
            String avatarPath = storeFile(avatar, "user");
            user.setAvatar(baseUrl + "/uploads/user/" + avatarPath);
        }

        User savedUser = userRepository.save(user);
        return createJwtResponse(savedUser, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Cast to Spring Security user, NOT your entity
            org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

            // If you want the full entity, load from DB
            User userEntity = userRepository.findByEmail(springUser.getUsername());

            return createJwtResponse(userEntity, HttpStatus.OK);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid email or password"));
        }
    }



    @Override
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = jwtTokenUtil.getCleanJwtCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("success", true, "message", "Logged out successfully"));
    }

    @Override
    public ResponseEntity<?> forgotPassword(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException("User not found with this email", HttpStatus.NOT_FOUND);
        }

        String resetToken = UUID.randomUUID().toString();
        user.setResetPasswordToken(resetToken);
        user.setResetPasswordTokenExpire(new Date(System.currentTimeMillis() + 30 * 60 * 1000)); // 30 minutes

        userRepository.save(user);

        String resetUrl = baseUrl + "/password/reset?token=" + resetToken;
        String message = "Your password reset URL is: " + resetUrl;

        emailService.sendEmail(user.getEmail(), "Password Recovery", message);

        return ResponseEntity.ok(Map.of("success", true, "message", "Email sent to " + user.getEmail()));
    }


    @Override
    public ResponseEntity<?> resetPassword(String token, PasswordResetDto passwordResetDto) {
        User user = userRepository.findByResetPasswordToken(token)
                .filter(u -> u.getResetPasswordTokenExpire().after(new Date()))
                .orElseThrow(() -> new CustomException("Password reset token is invalid or expired", HttpStatus.BAD_REQUEST));

        if (!passwordResetDto.getPassword().equals(passwordResetDto.getConfirmPassword())) {
            throw new CustomException("Passwords don't match", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(passwordResetDto.getPassword()));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenExpire(null);
        userRepository.save(user);

        return createJwtResponse(user, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserProfile() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Not authenticated"));
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;

                // Use email to find user since email is unique and used as username
                String email = userDetails.getUsername();  // This is the email in your case

                User user = userRepository.findByEmail(email); // Make sure this method exists!

                if (user == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "User not found"));
                }

                return ResponseEntity.ok(Map.of("success", true, "user", user));
            } else if (principal instanceof String && "anonymousUser".equals(principal)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Anonymous access not allowed"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "message", "Invalid principal type"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("success", false, "message", "Error retrieving profile"));
        }
    }


    @Override
    public ResponseEntity<?> changePassword(PasswordChangeDto passwordChangeDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "User not authenticated"));
        }

        UserDetails userDetails = (UserDetails) principal;
        User user = userRepository.findByEmail(userDetails.getUsername());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "User not found"));
        }

        // Null check for oldPassword and newPassword
        if (passwordChangeDto.getOldPassword() == null || passwordChangeDto.getNewPassword() == null) {
            return ResponseEntity.badRequest()
                .body(Map.of("success", false, "message", "Old password and new password must not be null"));
        }

        if (!passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "message", "Old password is incorrect"));
        }

        user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "message", "Password changed successfully"));
    }

    @Override
    public ResponseEntity<?> updateProfile(UserUpdateDto updateDto, MultipartFile avatar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername(); // usually username is email
        } else {
            throw new CustomException("Invalid user principal", HttpStatus.UNAUTHORIZED);
        }
       
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException("User not found", HttpStatus.NOT_FOUND);
        }
        if (updateDto.getName() != null && !updateDto.getName().isBlank()) {
            user.setName(updateDto.getName());
        }

        if (updateDto.getEmail() != null && !updateDto.getEmail().isBlank()) {
            user.setEmail(updateDto.getEmail());
        }

        if (avatar != null && !avatar.isEmpty()) {
            String avatarPath = storeFile(avatar, "user");
            user.setAvatar("http://localhost:8080" +"/uploads/user/" + avatarPath);
        }

        User updatedUser = userRepository.save(user);

        return ResponseEntity.ok(Map.of("success", true, "user", updatedUser));
    }


    @Override
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(Map.of("success", true, "users", users));
    }

    @Override
    public ResponseEntity<?> getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id: " + id, HttpStatus.NOT_FOUND));
        return ResponseEntity.ok(Map.of("success", true, "user", user));
    }

    @Override
    public ResponseEntity<?> updateUser(Long id, AdminUserUpdateDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        user.setName(updateDto.getName());
        user.setEmail(updateDto.getEmail());
        user.setRole(updateDto.getRole());

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(Map.of("success", true, "user", updatedUser));
    }

    @Override
    public ResponseEntity<?> deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        userRepository.delete(user);
        return ResponseEntity.ok(Map.of("success", true));
    }

    private String storeFile(MultipartFile file, String subDirectory) {
        try {
            Path uploadPath = Paths.get(uploadDir, subDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
            String fileExtension = originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : "";
            String fileName = UUID.randomUUID() + fileExtension;

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new CustomException("Could not store file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private ResponseEntity<?> createJwtResponse(User user, HttpStatus status) {
        String token = jwtTokenUtil.generateToken(user);
        ResponseCookie cookie = jwtTokenUtil.createJwtCookie(token);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("token", token);
        response.put("user", user);

        return ResponseEntity.status(status)
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(response);
    }
}
