package com.onlinestore.onlinestore.model;

import com.onlinestore.onlinestore.entity.UserEntity;

public class User {
    private Long id;
    private String role;
    private String name;

    public User() {
    }

    public static User toModel(UserEntity user) {
        User model = new User();
        model.setId((user.getId()));
        model.setName(user.getName());
        model.setRole(user.getRole());
        return model;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
