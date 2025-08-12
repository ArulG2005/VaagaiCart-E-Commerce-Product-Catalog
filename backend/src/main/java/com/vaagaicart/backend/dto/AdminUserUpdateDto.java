package com.vaagaicart.backend.dto;

public class AdminUserUpdateDto {
    private String name;
    private String email;
    private String role; // e.g., ADMIN, USER
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}

    // Getters and setters
}
