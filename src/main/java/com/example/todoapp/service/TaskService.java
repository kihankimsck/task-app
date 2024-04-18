package com.example.todoapp.service;

import com.example.todoapp.dao.TaskDao;
import com.example.todoapp.models.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {

    @Autowired
    TaskDao taskDao;

    public List<Task> getTasks() {
        return taskDao.getTasks();
    }

    public void insertTask(Task task) {
        taskDao.insertTask(task);
    }

    public void updateTask(Task task) {
        taskDao.updateTask(task);
    }

    public void deleteTask(Task task) {
        taskDao.deleteTask(task);
    }

    public Task getTask(long id) {
        return taskDao.getTask(id);
    }
}
