package com.example.TaskService.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    UserModel user_id;

    @ManyToOne
    UserModel creator_id;

    String task_name;

    String task_text;

    LocalDateTime notification_time;

    LocalDateTime deadline;
}
