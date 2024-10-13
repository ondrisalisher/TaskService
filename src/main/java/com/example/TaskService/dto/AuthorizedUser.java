package com.example.TaskService.dto;

public class AuthorizedUser {
    private static Long id;

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        AuthorizedUser.id = id;
    }
}
