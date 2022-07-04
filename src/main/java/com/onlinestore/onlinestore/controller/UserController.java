package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.UserCredsDTO;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDTO;
import com.onlinestore.onlinestore.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @PostMapping(value = "/registration")
    public ResponseEntity registration(@RequestBody UserCredsDTO user) {
        userServiceImpl.registerUser(user);

        return ResponseEntity.ok().body(new SuccessMessageDTO(SuccessMessage.USER_ADDED));
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            userServiceImpl.refreshToken(request, response);
        } catch (Exception e) {
            userServiceImpl.unsuccessfulRefresh(response, e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        userServiceImpl.logoutUser(request.getHeader(AUTHORIZATION));

        return ResponseEntity.ok().body(new SuccessMessageDTO(SuccessMessage.USER_LOGGED_OUT));
    }
}
