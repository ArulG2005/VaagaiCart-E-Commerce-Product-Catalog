package com.vaagaicart.backend.dto;

import java.util.Map;

public class PaymentRequest {

    private Long amount;
    private Map<String, Object> shipping;
   
    
    
    
 
	public PaymentRequest() {
    }

    public PaymentRequest(Long amount, Map<String, Object> shipping) {
        this.amount = amount;
        this.shipping = shipping;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Map<String, Object> getShipping() {
        return shipping;
    }

    public void setShipping(Map<String, Object> shipping) {
        this.shipping = shipping;
    }


}
