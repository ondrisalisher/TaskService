package com.example.TaskService.dto;

import com.example.TaskService.filters.TaskFilters;
import lombok.Data;

import java.util.List;

@Data
public class AllTasksRequest {
    private List<TaskFilters> filters;
}
