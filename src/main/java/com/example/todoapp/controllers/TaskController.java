package com.example.todoapp.controllers;

import com.example.todoapp.models.Task;
import com.example.todoapp.service.TaskService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Controller
public class TaskController {

    @Autowired
    TaskService taskService;

    @GetMapping("/moveToUpdate/{id}")
    public String moveToUpdateForm(@PathVariable("id") long id, Model model) {
        Task task = taskService.getTask(id);
        model.addAttribute("task", task);
        return "updateForm";
    }

    @GetMapping("/moveToCreate")
    public String moveToCreate(Task task) {
        return "createForm";
    }

    @PostMapping("/create")
    public String create(@Valid Task task, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "createForm";
        }

        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());

        // Mybatis
        taskService.insertTask(task);

        return "redirect:/";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") long id, @Valid Task task, BindingResult bindingResult, Model model) {
        //
        if (bindingResult.hasErrors()) {
            task.setId(id);
            return "updateForm";
        }
        //
        task.setUpdatedAt(LocalDateTime.now());

        // Mybatis
        taskService.updateTask(task);

        //
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id, Model model) {

        //
        Optional<Task> task = Optional.ofNullable(taskService.getTask(id));
        if (task.isPresent()) {
            taskService.deleteTask(task.get());
        } else {
            throw new IllegalArgumentException("존재하지 않는 태스크 입니다.");
        }

        return "redirect:/";
    }

    @GetMapping("/{age}")
    public String combinedurl(@PathVariable("age") int age, @RequestParam("name") String name) {
        log.info(String.format("age -> %d, name : %s", age, name));
        return "redirect:/";
    }
}

