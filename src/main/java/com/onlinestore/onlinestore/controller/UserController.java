package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.constants.ErrorMessage;
import com.onlinestore.onlinestore.constants.SuccessMessage;
import com.onlinestore.onlinestore.dto.request.UserLogoutDto;
import com.onlinestore.onlinestore.dto.request.UserLoginDto;
import com.onlinestore.onlinestore.dto.request.UserRegistrationDto;
import com.onlinestore.onlinestore.dto.response.ErrorMessageDto;
import com.onlinestore.onlinestore.dto.response.SuccessMessageDto;
import com.onlinestore.onlinestore.dto.response.UserDto;
import com.onlinestore.onlinestore.exception.*;
import com.onlinestore.onlinestore.service.TokenService;
import com.onlinestore.onlinestore.service.UserService;
import com.onlinestore.onlinestore.utility.CookieUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;

    UserController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @PostMapping(value = "/registration")
    public ResponseEntity registration(@RequestBody UserRegistrationDto user) {
        try {
            userService.registerUser(user);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.USER_ADDED),
                    HttpStatus.OK
            );
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.USER_EXISTS),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity authorization(@RequestBody UserLoginDto userLoginDto, HttpServletResponse response) throws UserLoginPasswordIncorrectException {
        try {
            UserDto userDto = userService.authorizeUser(userLoginDto);
            String token = tokenService.getTokenByUser(userDto);
            Cookie cookie = CookieUtils.getCookie("token", token);

            response.addCookie(cookie);

            return new ResponseEntity(userDto, HttpStatus.OK);
        } catch (UserLoginPasswordIncorrectException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.LOGIN_OR_PASSWORD_INCORRECT),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping(value = "/user-info")
    public ResponseEntity userInfo(@CookieValue("token") String token) {
        try {
            UserDto userDto = userService.getUserInfo(token);

            return new ResponseEntity(userDto, HttpStatus.OK);
        } catch (InvalidTokenExceptionException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.UNAUTHORIZED),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping(value = "/logout")
    public ResponseEntity logout(@RequestBody UserLogoutDto user) {
        try {
            userService.logoutUser(user);

            return new ResponseEntity(
                    new SuccessMessageDto(SuccessMessage.USER_LOGGED_OUT),
                    HttpStatus.OK
            );
        } catch (UserNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.USER_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        } catch (TokenNotFoundException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.TOKEN_NOT_FOUND),
                    HttpStatus.BAD_REQUEST
            );
        } catch (RuntimeException e) {
            e.printStackTrace();

            return new ResponseEntity(
                    new ErrorMessageDto(ErrorMessage.INTERNAL_SERVER_ERROR),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

    }
}
