package com.example.todoapp.todo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Todo {
    private Long id; //
    @NotBlank(message = "할일은 필수입력값 입니다.")
    private String description;
    private boolean complete; //
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
