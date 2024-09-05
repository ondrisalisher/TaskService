package com.example.TaskService.controllers;

import com.example.TaskService.services.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NoteController {

    NoteService noteService;
    public ResponseEntity<?> addTask(){
        return noteService.addTask();
    }
}
