package com.example.TaskService.dto;

import lombok.Data;

import java.util.Date;

@Data
public class EditTaskRequest {
    private String taskIdAsString;
    private String title;
    private String text;
    private String executionerIdAsString;
    private Date startsAt;
    private Date deadline;
    private Date remindAt;
}
