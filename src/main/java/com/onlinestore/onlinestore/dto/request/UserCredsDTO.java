package com.onlinestore.onlinestore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserCredsDTO {
    private String username;
    private String password;
}
