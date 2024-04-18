package com.example.todoapp;

public class User {
    private static final User instance = new User();

    private User() {
    }

    public static User getInstance() {
        return instance;
    }

    public void singleton() {
        System.out.println("User");
    }
}