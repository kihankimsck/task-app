package com.example.todoapp.todo.service;

import com.example.todoapp.todo.dao.TodoDao;
import com.example.todoapp.todo.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    TodoDao todoDao;

    public Todo getTask(int id) {
        return todoDao.getTask(id);
    }

    public void create(Todo todo) {
        todoDao.create(todo);
    }

    public List<Todo> getTodos() {
        return todoDao.getTodos();
    }
}
