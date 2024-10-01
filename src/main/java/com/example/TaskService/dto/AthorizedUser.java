package com.example.TaskService.dto;

import lombok.Data;

public class AthorizedUser {
    public static String username;

    public static String getUsername() {
        return username;
    }
    public static void setUsername(String username) {
        AthorizedUser.username = username;
    }
}
