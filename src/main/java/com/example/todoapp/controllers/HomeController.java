package com.example.todoapp.controllers;

import com.example.todoapp.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Controller
public class HomeController {

    @Autowired
    TaskService taskService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tasks", taskService.getTasks());
        model.addAttribute("today", LocalDateTime.now().atZone(ZoneId.systemDefault()).toLocalDateTime().getDayOfWeek());
        return "index";
    }
}
