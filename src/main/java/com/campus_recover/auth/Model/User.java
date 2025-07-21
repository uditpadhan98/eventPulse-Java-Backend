package com.campus_recover.auth.Model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users") // MongoDB collection name
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private UserType type;
    private String club; // ✅ Only required for organiser

    // Constructors
    public User() {}

    public User(String id, String club, UserType type, String password, String email, String name) {
        this.id = id;
        this.club = club;
        this.type = type;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public String getClub() {
        return club;
    }

    public void setClub(String club) {
        this.club = club;
    }
}
