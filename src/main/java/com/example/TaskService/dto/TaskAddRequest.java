package com.example.TaskService.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class TaskAddRequest {
    private String title;
    private String text;
    private String executioner;
    private Date startsAt;
    private Date deadline;
    private Date remindAt;
}
