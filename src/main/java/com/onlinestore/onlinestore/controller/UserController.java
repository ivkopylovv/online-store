package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.UserRegistrationDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.exception.TokenNotFoundException;
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.exception.UserNotFoundException;
import com.onlinestore.onlinestore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final UserService userService;

    @PostMapping(value = "/registration")
    public ResponseEntity registration(@RequestBody UserRegistrationDto user) {
        try {
            userService.registerUser(user);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.USER_ADDED),
                    HttpStatus.OK
            );
        } catch (UserAlreadyExistException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            userService.refreshToken(request, response);
        } catch (TokenNotFoundException e) {
            userService.unsuccessfulRefresh(response, e);
        } catch (Exception e) {
            userService.unsuccessfulRefresh(response, e);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        try {
            userService.logoutUser(request.getHeader(AUTHORIZATION));

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.USER_LOGGED_OUT),
                    HttpStatus.OK
            );
        } catch (UserNotFoundException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
