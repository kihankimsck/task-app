package com.example.todoapp.todo.service;

import com.example.todoapp.todo.dao.TodoDao;
import com.example.todoapp.todo.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    TodoDao todoDao;

    public Todo getTask(int id) {
        return todoDao.getTask(id);
    }
}
