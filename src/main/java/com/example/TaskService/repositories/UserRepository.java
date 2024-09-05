package com.example.TaskService.repositories;

import com.example.TaskService.models.UserModel;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaSpecificationExecutor<UserModel> {
}
