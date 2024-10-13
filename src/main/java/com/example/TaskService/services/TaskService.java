package com.example.TaskService.services;

import com.example.TaskService.dto.AllTasksRequest;
import com.example.TaskService.dto.EditTaskRequest;
import com.example.TaskService.dto.AddTaskRequest;
import com.example.TaskService.dto.RequestWithId;
import org.springframework.http.ResponseEntity;

public interface TaskService {
    public ResponseEntity<?> addTask(AddTaskRequest addTaskRequest);
    public ResponseEntity<?> deleteTask(RequestWithId requestWithId);
    public ResponseEntity<?> editTask(EditTaskRequest editTaskRequest);
    public ResponseEntity<?> allTasks(AllTasksRequest allTasksRequest);
    public ResponseEntity<?> completeTask(RequestWithId requestWithId);
    public ResponseEntity<?> incompleteTask(RequestWithId requestWithId);

}
