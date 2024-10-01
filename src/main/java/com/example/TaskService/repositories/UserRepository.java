package com.example.TaskService.repositories;

import com.example.TaskService.models.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaSpecificationExecutor<User>, CrudRepository<User, Long>{
    Optional<User> findByUsername(String username);
}
