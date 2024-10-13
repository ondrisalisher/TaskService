package com.example.TaskService.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "executioner_id")
    private User executioner;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;

    private String title;

    private String text;

    @Column(name = "startsAt")
    private Date startsAt;

    @Column(name = "remind_at")
    private Date remindAt;

    private Date deadline;

    private boolean isCompleted;
}
