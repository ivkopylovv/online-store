package com.onlinestore.onlinestore.dto.response;

import com.onlinestore.onlinestore.entity.UserEntity;

public class UserDto {
    private Long id;
    private String role;
    private String name;

    public UserDto() {
    }

    public UserDto(UserEntity user) {
        this.id = user.getId();
        this.name = user.getName();
        this.role = "ROLE_USER";
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
