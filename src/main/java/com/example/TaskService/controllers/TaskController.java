package com.example.TaskService.controllers;

import com.example.TaskService.dto.TaskAddRequest;
import com.example.TaskService.services.TaskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/task")
public class TaskController {
    TaskService taskService;

    @GetMapping("/addTask")
        public ResponseEntity<?> addTask(@RequestBody TaskAddRequest taskAddRequest){
        return taskService.addTask(taskAddRequest);
    }
}
