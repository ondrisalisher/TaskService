package com.example.TaskService.services.impl;

import com.example.TaskService.dto.AthorizedUser;
import com.example.TaskService.dto.NotificationDto;
import com.example.TaskService.dto.TaskAddRequest;
import com.example.TaskService.models.Task;
import com.example.TaskService.models.User;
import com.example.TaskService.repositories.TaskRepository;
import com.example.TaskService.repositories.UserRepository;
import com.example.TaskService.services.TaskService;
import com.example.TaskService.utils.KafkaUtils;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final KafkaUtils kafkaUtils;
    @Value("${kafka.notification-topic}")
    private String notificationKafkaTopic;


    @Override
    public ResponseEntity<?> addTask(TaskAddRequest taskAddRequest) {
        String creatorUsername = AthorizedUser.username;
        User creator = userRepository.findByUsername(creatorUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + creatorUsername));

        User executioner = userRepository.findByUsername(taskAddRequest.getExecutioner())
                .orElseThrow(() -> new RuntimeException("Executioner not found: " + taskAddRequest.getExecutioner()));


        Task task = Task.builder()
                .title(taskAddRequest.getTitle())
                .text(taskAddRequest.getText())
                .executioner(executioner)
                .creator(creator)
                .deadline(taskAddRequest.getDeadline())
                .remindAt(taskAddRequest.getRemindAt())
                .startsAt(taskAddRequest.getStartsAt())
                .build();
        taskRepository.save(task);


        NotificationDto notification = NotificationDto.builder()
                .title(taskAddRequest.getTitle())
                .text(taskAddRequest.getText())
                .executioner(taskAddRequest.getExecutioner())
                .creator(creatorUsername)
                .deadline(taskAddRequest.getDeadline())
                .remindAt(taskAddRequest.getRemindAt())
                .startsAt(taskAddRequest.getStartsAt())
                .build();

        kafkaUtils.send(notificationKafkaTopic, notification);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<?> deleteTask() {

        return null;
    }

    @Override
    public ResponseEntity<?> myTasks() {
        return null;
    }

    @Override
    public ResponseEntity<?> completeTask() {
        return null;
    }
}
