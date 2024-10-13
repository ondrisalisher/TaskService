package com.example.TaskService.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AddTaskRequest {
    private String title;
    private String text;
    private Long executioner;
    private Date startsAt;
    private Date deadline;
    private Date remindAt;
}
