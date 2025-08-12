package com.vaagaicart.backend.controller;


import com.stripe.model.PaymentIntent;
import com.vaagaicart.backend.dto.PaymentRequest;
import com.vaagaicart.backend.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/payment/process")
    public ResponseEntity<Map<String, String>> processPayment(@RequestBody PaymentRequest paymentRequest) throws Exception {
        // Convert paymentRequest.amount and shipping info as needed

        PaymentIntent paymentIntent = paymentService.processPayment(paymentRequest.getAmount(), paymentRequest.getShipping());

        Map<String, String> responseData = new HashMap<>();
        responseData.put("client_secret", paymentIntent.getClientSecret());

        return ResponseEntity.ok(responseData);
    }


    @GetMapping("/stripeapi")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getStripeApiKey() {
        return ResponseEntity.ok(Map.of(
            "stripeApiKey", paymentService.getStripeApiKey()
        ));
    }
}
