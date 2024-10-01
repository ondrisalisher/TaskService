package com.example.TaskService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDto {
    private String title;
    private String text;
    private String executioner;
    private String creator;
    private Date startsAt;
    private Date deadline;
    private Date remindAt;
}
