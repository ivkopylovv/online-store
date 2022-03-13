package com.onlinestore.onlinestore.entity;

import com.onlinestore.onlinestore.constants.UserRole;

import javax.persistence.*;

@Entity
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String login;
    private String password;
    private String role;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private TokenEntity token;

    public UserEntity() {
    }

    public UserEntity(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.role = UserRole.USER;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setToken(TokenEntity token) {
        this.token = token;
    }
}
