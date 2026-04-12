package com.example.jpa.interview.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String bio;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    protected UserProfile() {
    }

    public UserProfile(String bio) {
        this.bio = bio;
    }

    void internalSetUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public User getUser() {
        return user;
    }
}
