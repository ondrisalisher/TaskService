package com.example.TaskService.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data

@Entity
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "executioner_id")
    User executioner;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    User creator;

    String title;

    String text;

    @Column(name = "startsAt")
    Date startsAt;

    @Column(name = "remind_at")
    Date remindAt;

    Date deadline;
}
