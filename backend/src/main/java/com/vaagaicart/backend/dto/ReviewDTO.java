package com.vaagaicart.backend.dto;


public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;
    private UserDTO user;

    public ReviewDTO() {}

    public ReviewDTO(Long id, int rating, String comment, UserDTO user) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
