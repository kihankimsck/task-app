package com.example.todoapp.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Task {
    private Long id;
    private String description;
    private boolean complete;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
