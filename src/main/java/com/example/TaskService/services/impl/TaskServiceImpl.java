package com.example.TaskService.services.impl;

import com.example.TaskService.dto.*;
import com.example.TaskService.filters.TaskFilters;
import com.example.TaskService.models.Task;
import com.example.TaskService.models.User;
import com.example.TaskService.repositories.TaskRepository;
import com.example.TaskService.repositories.UserRepository;
import com.example.TaskService.services.TaskService;
import com.example.TaskService.utils.KafkaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final KafkaUtils kafkaUtils;
    @Value("${kafka.notification-topic}")
    private String notificationKafkaTopic;
    private final RedisTemplate<String, Object> redisTemplate;
    private final int redisTTL = 5;
    private final TimeUnit redisTimeUnit = TimeUnit.MINUTES;
    @Value("${redis.lifetime}")
    private Duration redisLifetime;



    @Override
    public ResponseEntity<?> addTask(AddTaskRequest addTaskRequest) {
        log.info("Service add task is called");

        Long creatorId = AuthorizedUser.getId();
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found: " + creatorId));

        User executioner = userRepository.findById(addTaskRequest.getExecutioner())
                .orElseThrow(() -> new RuntimeException("Executioner not found: " + addTaskRequest.getExecutioner()));


        Task task = Task.builder()
                .title(addTaskRequest.getTitle())
                .text(addTaskRequest.getText())
                .executioner(executioner)
                .creator(creator)
                .deadline(addTaskRequest.getDeadline())
                .remindAt(addTaskRequest.getRemindAt())
                .startsAt(addTaskRequest.getStartsAt())
                .isCompleted(false)
                .build();
        taskRepository.save(task);


        NotificationDto notification = NotificationDto.builder()
                .title(addTaskRequest.getTitle())
                .text(addTaskRequest.getText())
                .executioner(executioner.getId())
                .creator(creator.getId())
                .deadline(addTaskRequest.getDeadline())
                .remindAt(addTaskRequest.getRemindAt())
                .startsAt(addTaskRequest.getStartsAt())
                .build();

        kafkaUtils.send(notificationKafkaTopic, notification);
        if(redisTemplate.opsForValue().get(AuthorizedUser.getId().toString()) != null){
            redisTemplate.delete(AuthorizedUser.getId().toString());
        }

        log.info("Service add task is successfully completed");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteTask(RequestWithId requestWithId) {
        log.info("Service delete task is called");

        Optional<Task> taskOptional = taskRepository.findById(Long.parseLong(requestWithId.getIdAsString()));
        if(taskOptional.isEmpty()){
            throw new RuntimeException("Task not exist");
        }
        Task task = taskOptional.get();
        if(!AuthorizedUser.getId().equals(task.getCreator().getId()) && !AuthorizedUser.getId().equals(task.getExecutioner().getId())){
            throw new RuntimeException("Not enough rights");
        }
        taskRepository.delete(task);
        if(redisTemplate.opsForValue().get(AuthorizedUser.getId().toString()) != null){
            redisTemplate.delete(AuthorizedUser.getId().toString());
        }

        log.info("Service delete task is successfully completed");
        return null;
    }

    @Override
    public ResponseEntity<?> editTask(EditTaskRequest editTaskRequest) {
        log.info("Service edit task is called");

        Optional<Task> taskOptional = taskRepository.findById(Long.parseLong(editTaskRequest.getTaskIdAsString()));
        if(taskOptional.isEmpty()){
            throw new RuntimeException("Task not found");
        }
        Task task = taskOptional.get();
        if(!task.getCreator().getId().equals(AuthorizedUser.getId())){
            throw new RuntimeException("User is not creator");
        }
        if(task.isCompleted()){
            throw new RuntimeException("Task is already done");
        }

        task.setTitle(editTaskRequest.getTitle());
        task.setText(editTaskRequest.getText());
        Optional<User> executionerOptional = userRepository.findById(Long.parseLong(editTaskRequest.getExecutionerIdAsString()));
        if(executionerOptional.isEmpty()){
            throw new RuntimeException("Executioner not found");
        }
        task.setExecutioner(executionerOptional.get());
        task.setStartsAt(editTaskRequest.getStartsAt());
        task.setRemindAt(editTaskRequest.getRemindAt());
        task.setDeadline(editTaskRequest.getDeadline());
        taskRepository.save(task);

        if(redisTemplate.opsForValue().get(AuthorizedUser.getId().toString()) != null){
            redisTemplate.delete(AuthorizedUser.getId().toString());
        }

        log.info("Service edit task is successfully completed");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> allTasks(AllTasksRequest allTasksRequest) {
        log.info("Service all tasks is called");

        User currentUser = userRepository.findById(AuthorizedUser.getId()).get();
        if(redisTemplate.opsForValue().get(AuthorizedUser.getId().toString()) != null){
            List<Task> tasks = (List<Task>) redisTemplate.opsForValue().get(AuthorizedUser.getId().toString());
        }else{
            List<Task> tasks = taskRepository.findAll(findAllTasksByUser(currentUser));
            redisTemplate.opsForValue().set(AuthorizedUser.getId().toString(), tasks);
            redisTemplate.expire(AuthorizedUser.getId().toString(),redisLifetime);
        }

        List<Task> tasks = taskRepository.findAll(findAllTasksByUser(currentUser));
        List<TaskFilters> filters = allTasksRequest.getFilters();
        if(filters != null){
            if (filters.contains(TaskFilters.CREATOR)){
                tasks = tasks.stream().filter(task ->
                                task.getCreator().equals(currentUser))
                        .toList();
            }
            if (filters.contains(TaskFilters.EXECUTIONER)){
                tasks = tasks.stream().filter(task ->
                                task.getExecutioner().equals(currentUser))
                        .toList();
            }
            if (filters.contains(TaskFilters.STARTED)){
                tasks = tasks.stream().filter(task ->
                                task.getStartsAt().before(new Date()))
                        .toList();
            }
            if (filters.contains(TaskFilters.EXPIRED)){
                tasks = tasks.stream().filter(task ->
                                task.getDeadline().before(new Date()))
                        .toList();
            }
            if (filters.contains(TaskFilters.NOT_EXPIRED)){
                tasks = tasks.stream().filter(task ->
                                task.getDeadline().after(new Date()))
                        .toList();
            }
            if (filters.contains(TaskFilters.COMPLETED)){
                tasks = tasks.stream().filter(Task::isCompleted)
                        .toList();
            }
            if (filters.contains(TaskFilters.NOT_COMPLETED)){
                tasks = tasks.stream().filter(task ->
                                !task.isCompleted())
                        .toList();
            }
        }

        log.info("Service all tasks is successfully completed");
        return ResponseEntity.ok(tasks);
    }

    @Override
    public ResponseEntity<?> completeTask(RequestWithId requestWithId) {
        log.info("Service complete task is called");

        Optional<Task> taskOptional = taskRepository.findById(Long.parseLong(requestWithId.getIdAsString()));
        if(taskOptional.isEmpty()){
            throw new RuntimeException("Task is not exists");
        }
        Task task = taskOptional.get();
        if(!AuthorizedUser.getId().equals(task.getExecutioner().getId())){
            throw new RuntimeException("User is not executioner");
        }
        task.setCompleted(true);
        taskRepository.save(task);

        if(redisTemplate.opsForValue().get(AuthorizedUser.getId().toString()) != null){
            redisTemplate.delete(AuthorizedUser.getId().toString());
        }

        log.info("Service complete task is successfully completed");
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> incompleteTask(RequestWithId requestWithId) {
        log.info("Service incomplete task is called");

        Optional<Task> taskOptional = taskRepository.findById(Long.parseLong(requestWithId.getIdAsString()));
        if(taskOptional.isEmpty()){
            throw new RuntimeException("Task is not exists");
        }
        Task task = taskOptional.get();
        if(!AuthorizedUser.getId().equals(task.getExecutioner().getId())){
            throw new RuntimeException("User is not executioner");
        }
        task.setCompleted(false);
        taskRepository.save(task);

        if(redisTemplate.opsForValue().get(AuthorizedUser.getId().toString()) != null){
            redisTemplate.delete(AuthorizedUser.getId().toString());
        }

        log.info("Service incomplete task is successfully completed");
        return ResponseEntity.ok().build();
    }


    private static Specification<Task> findAllTasksByUser(User user) {
        Specification<Task> byExecutioner = (root, query, builder) -> builder.equal(root.get("executioner").as(Long.class), user.getId());
        Specification<Task> byCreator = (root, query, builder) -> builder.equal(root.get("creator").as(Long.class), user.getId());
        return  byExecutioner.or(byCreator);
    }
}
