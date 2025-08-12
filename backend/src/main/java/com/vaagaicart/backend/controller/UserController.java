package com.vaagaicart.backend.controller;



import com.vaagaicart.backend.dto.*;
import com.vaagaicart.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1")
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Register user with avatar
    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerUser(
        @ModelAttribute UserRegistrationDto registrationDto,
        @RequestPart(value = "avatar", required = false) MultipartFile avatar) {
        
        return userService.registerUser(registrationDto, avatar);
    }


    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginDto loginDto) {

    		
    	
        return userService.authenticateUser(loginDto);
    }

    // ✅ Logout
    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        return userService.logout();
    }

    // ✅ Forgot password
    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
        System.out.println(email);
    	return userService.forgotPassword(email);
    }

    // ✅ Reset password
    @PostMapping("/password/reset/{token}")
    public ResponseEntity<?> resetPassword(
            @PathVariable String token,
            @RequestBody PasswordResetDto passwordResetDto) {
        return userService.resetPassword(token, passwordResetDto);
    }

    // ✅ Change password
    @PutMapping("/password/change")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        return userService.changePassword(passwordChangeDto);
    }

    // ✅ Get user profile
    @GetMapping("/myprofile")
    public ResponseEntity<?> getUserProfile() {
        return userService.getUserProfile();
    }

    // ✅ Update profile with avatar
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(
            @ModelAttribute UserUpdateDto updateDto,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        return userService.updateProfile(updateDto, avatar);
    }

    // ------------------- ADMIN ROUTES -------------------

    // ✅ Get all users (Admin only)
    @GetMapping("/admin/users")
    public ResponseEntity<?> getAllUsers() {
        return userService.getAllUsers();
    }

    // ✅ Get single user by ID (Admin)
    @GetMapping("/admin/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    // ✅ Update user (Admin)
    @PutMapping("/admin/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody AdminUserUpdateDto updateDto) {
        return userService.updateUser(id, updateDto);
    }

    // ✅ Delete user (Admin)
    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
