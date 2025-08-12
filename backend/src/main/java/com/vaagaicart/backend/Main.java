package com.vaagaicart.backend;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Main {

	    public static void main(String[] args) {
	        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	        System.out.println(encoder.encode("arul"));
	    }
	}


