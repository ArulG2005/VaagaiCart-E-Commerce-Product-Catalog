package com.vaagaicart.backend.entity;



import jakarta.persistence.Embeddable;

@Embeddable
public class ShippingInfo {
	private String Name;
    private String address;
    private String country;
    private String city;
    private String phoneNo;
    private String state;
    
    private String postalCode;

    
    
    
    public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public ShippingInfo() {
    }

    // Getters and Setters

    public String getAddress() {
        return address;
    }

    public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

	
}
