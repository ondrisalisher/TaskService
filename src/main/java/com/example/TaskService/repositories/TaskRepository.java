package com.example.TaskService.repositories;

import com.example.TaskService.models.TaskModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaSpecificationExecutor<TaskModel> {
}
