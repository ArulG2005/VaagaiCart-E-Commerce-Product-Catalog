package com.vaagai.backend.dto;

public class LoginRequestDTO {
	 private String email;
	    private String password;

	    public String getEmail() {
	        return email;
	    }

	    public void setEmailId(String emailId) {
	        this.email = emailId;
	    }

	    public String getPassword() {
	        return password;
	    }

	    public void setPassword(String password) {
	        this.password = password;
	    }
}
