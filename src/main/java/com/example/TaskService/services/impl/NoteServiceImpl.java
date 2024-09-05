package com.example.TaskService.services.impl;

import com.example.TaskService.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;

public class NoteServiceImpl implements NoteService {
    @Override
    public ResponseEntity<?> addTask() {
        if(SecurityContextHolder.getContext().getAuthentication() == null){
//            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                    username,
//                    null,
//                    jwtUtils.getRoles(jwt).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
//            );
//            SecurityContextHolder.getContext().setAuthentication(token);
            String string = SecurityContextHolder.getContext().getAuthentication().getName();
            System.out.println(string);
        }
        else {
            System.out.println("No");
        }
        return null;
    }

    @Override
    public ResponseEntity<?> deleteTask() {
        return null;
    }

    @Override
    public ResponseEntity<?> myTasks() {
        return null;
    }

    @Override
    public ResponseEntity<?> completeTask() {
        return null;
    }
}
