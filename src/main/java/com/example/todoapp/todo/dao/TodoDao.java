package com.example.todoapp.todo.dao;

import com.example.todoapp.todo.model.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TodoDao {
    Todo getTask(int id);

    void create(Todo todo);

    List<Todo> getTodos();
}
