package com.example.TaskService.services;

import com.example.TaskService.dto.TaskAddRequest;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    public ResponseEntity<?> addTask(TaskAddRequest taskAddRequest);
    public ResponseEntity<?> deleteTask();
    public ResponseEntity<?> myTasks();
    public ResponseEntity<?> completeTask();
}
