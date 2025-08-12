package com.vaagaicart.backend.serviceImpl;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.vaagaicart.backend.service.PaymentService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    @Override
    public PaymentIntent processPayment(Long amount, Map<String, Object> shipping) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", "usd");
        params.put("description", "TEST PAYMENT");

        Map<String, String> metadata = new HashMap<>();
        metadata.put("integration_check", "accept_payment");
        params.put("metadata", metadata);

        if (shipping != null) {
            // Extract nested maps from input
            String name = (String) shipping.get("name");
            String phone = (String) shipping.get("phone");
            Map<String, Object> addressMap = (Map<String, Object>) shipping.get("address");

            // Build the address map as Stripe expects
            Map<String, Object> stripeAddress = new HashMap<>();
            if (addressMap != null) {
                stripeAddress.put("line1", addressMap.get("line1"));
                stripeAddress.put("city", addressMap.get("city"));
                stripeAddress.put("state", addressMap.get("state"));
                stripeAddress.put("postal_code", addressMap.get("postal_code"));
                stripeAddress.put("country", addressMap.get("country"));
            }

            // Build the shipping map as Stripe expects
            Map<String, Object> stripeShipping = new HashMap<>();
            stripeShipping.put("name", name);
            stripeShipping.put("phone", phone);
            stripeShipping.put("address", stripeAddress);

            params.put("shipping", stripeShipping);
        }

        return PaymentIntent.create(params);
    }


    @Override
    public String getStripeApiKey() {
        return stripeApiKey;
    }
}
