package com.example.TaskService.services;

import org.springframework.http.ResponseEntity;

public interface NoteService {
    public ResponseEntity<?> addTask();
    public ResponseEntity<?> deleteTask();
    public ResponseEntity<?> myTasks();
    public ResponseEntity<?> completeTask();
}
