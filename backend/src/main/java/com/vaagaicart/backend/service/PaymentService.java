package com.vaagaicart.backend.service;


import com.stripe.model.PaymentIntent;
import java.util.Map;

public interface PaymentService {

    PaymentIntent processPayment(Long amount, Map<String, Object> shipping) throws Exception;

    String getStripeApiKey();
}
