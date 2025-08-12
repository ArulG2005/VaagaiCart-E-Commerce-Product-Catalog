package com.vaagaicart.backend.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PaymentInfo {

    @Column(name = "payment_id")
    private String id;
    private String status;

    public PaymentInfo() {
    }

    // Getters and Setters
    
    

    public String getId() {
        return id;
    }

    public PaymentInfo(String id, String status) {
		super();
		this.id = id;
		this.status = status;
	}

	public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
