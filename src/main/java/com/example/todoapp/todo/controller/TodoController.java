package com.example.todoapp.todo.controller;

import com.example.todoapp.todo.model.Todo;
import com.example.todoapp.todo.service.TodoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/")
    public String index(Model model) {
        // 1. get todos
        List<Todo> list = todoService.getTodos();

        // 2. set model attributes
        model.addAttribute("todos",  list);

        // 3. return view
        return "index";
    }

    @GetMapping("/moveToCreate")
    public String moveToCreate(Model model) {
        Todo todo = new Todo();
        model.addAttribute("todo", todo);
        // return create form
        return "createForm";
    }

    @GetMapping("/moveToUpdate/{id}")
    public String moveToUpdate(@PathVariable("id") int id) {
        // 1. get todo

        // 2. model로 todo 정보 전달

        // 3. return view
        return "updateForm";
    }



    @PostMapping("/create")
    public String create(@Valid Todo todo, BindingResult bindingResult) {
        // 1. validation check
        // 2. handle validation check result has error
        if (bindingResult.hasErrors()) {
            return "createForm";
        }

        // 3. set todo
        todo.setCreatedAt(LocalDateTime.now());
        todo.setUpdatedAt(LocalDateTime.now());

        // 4. insert todo
        todoService.create(todo);

        // 5. return view
        return "redirect:/todo/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {

        // 1. get todo

        // 2. throw error if the todo not exists

        // 3. delete the todo

        // 4. redirect view
        return "redirect:/todo/";
    }


    @PostMapping("/update/{id}")
    public String update(@PathVariable("id") int id) {
        return "redirect:/todo/";
    }
}
