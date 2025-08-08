package com.vaagai.backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaagai.backend.ExceptionHandler.APIResponse;
import com.vaagai.backend.dto.LoginRequestDTO;
import com.vaagai.backend.dto.SignUpRequestDTO;
import com.vaagai.backend.entity.User;
import com.vaagai.backend.repository.UserRepository;
import com.vaagai.backend.security.JwtUtils;

@Service
public class LoginService {

	 @Autowired
	    private UserRepository userRepository;
	    @Autowired
	    private JwtUtils jwtUtils;
	    @Autowired
	    private PasswordEncoder passwordEncoder;
	    
	    public APIResponse signUp(SignUpRequestDTO signUpRequestDTO) {
	        APIResponse apiResponse = new APIResponse();

	        // validation

	        // dto to entity
	        User userEntity = new User();
	        userEntity.setEmail(signUpRequestDTO.getEmail());
	        userEntity.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
	        userEntity.setAvatar(signUpRequestDTO.getAvatar());
	        
	        // store entity
	        userEntity = userRepository.save(userEntity);

	        // generate jwt
	        String token = jwtUtils.generateJwt(userEntity);

	        Map<String , Object> data = new HashMap<>();
	        data.put("accessToken", token);

	        apiResponse.setData(data);

	        return apiResponse;
	    }
	    public APIResponse login(LoginRequestDTO loginRequestDTO) {
	        APIResponse apiResponse = new APIResponse();

	        // Step 1: Find user by email
	        User user = userRepository.findOneByEmailIgnoreCase(loginRequestDTO.getEmail());

	        // Step 2: Validate user and password
	        if (user == null || !passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())) {
	            throw new RuntimeException("Invalid email or password");
	        }

	        // Step 3: Generate JWT token
	        String token = jwtUtils.generateJwt(user);

	        // Step 4: Prepare response
	        Map<String, Object> data = new HashMap<>();
	        data.put("accessToken", token);

	        apiResponse.setData(data);
	        return apiResponse;
	    }

}
