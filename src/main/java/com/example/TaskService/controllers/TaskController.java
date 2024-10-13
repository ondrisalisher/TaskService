package com.example.TaskService.controllers;

import com.example.TaskService.dto.AllTasksRequest;
import com.example.TaskService.dto.AddTaskRequest;
import com.example.TaskService.dto.EditTaskRequest;
import com.example.TaskService.dto.RequestWithId;
import com.example.TaskService.services.TaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/task")
@Slf4j
public class TaskController {
    TaskService taskService;
    @PostMapping("/addTask")
    public ResponseEntity<?> addTask(@RequestBody AddTaskRequest addTaskRequest){
        log.info("Controller add task is called");
        return taskService.addTask(addTaskRequest);
    }


    @GetMapping()
    public ResponseEntity<?> tasks(@RequestBody AllTasksRequest allTasksRequest){
        log.info("Controller tasks is called");
        return taskService.allTasks(allTasksRequest);
    }

    @PutMapping()
    public ResponseEntity<?> editTask(@RequestBody EditTaskRequest editTaskRequest){
        log.info("Controller edit task is called");
        return taskService.editTask(editTaskRequest);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteTask(@RequestBody RequestWithId requestWithId){
        log.info("Controller delete task is called");
        return taskService.deleteTask(requestWithId);
    }

    @PutMapping("/complete")
    public ResponseEntity<?> completeTask(@RequestBody RequestWithId requestWithId){
        log.info("Controller complete task is called");
        return taskService.completeTask(requestWithId);
    }

    @PutMapping("/incomplete")
    public ResponseEntity<?> incompleteTask(@RequestBody RequestWithId requestWithId){
        log.info("Controller incomplete task is called");
        return taskService.incompleteTask(requestWithId);
    }
}
