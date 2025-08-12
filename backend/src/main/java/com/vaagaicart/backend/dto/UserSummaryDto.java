package com.vaagaicart.backend.dto;


public class UserSummaryDto {
    private Long id;
    private String name;

    public UserSummaryDto() {}
    public UserSummaryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
