package com.onlinestore.onlinestore.dto.request;

public class UserRegistrationDto {
    private String name;
    private String login;
    private String password;

    public UserRegistrationDto() {

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
}
