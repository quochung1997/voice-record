package com.example.voice.models;

public class User {
    String id;
    String gender;

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User(String id, String gender) {
        this.id = id;
        this.gender = gender;
    }
}
