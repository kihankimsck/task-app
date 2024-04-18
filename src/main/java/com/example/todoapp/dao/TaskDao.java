package com.example.todoapp.dao;

import com.example.todoapp.models.Task;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface TaskDao {
    List<Task> getTasks();

    int insertTask(Task task);

    void updateTask(Task task);

    void deleteTask(Task task);

    Task getTask(long id);
}
