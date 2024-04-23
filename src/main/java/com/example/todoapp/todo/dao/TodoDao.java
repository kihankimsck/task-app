package com.example.todoapp.todo.dao;

import com.example.todoapp.todo.model.Todo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface TodoDao {
    Todo getTask(int id);
}
