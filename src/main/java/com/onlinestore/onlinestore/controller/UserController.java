package com.onlinestore.onlinestore.controller;

import com.onlinestore.onlinestore.entity.UserEntity;
import com.onlinestore.onlinestore.exception.UserAlreadyExistException;
import com.onlinestore.onlinestore.exception.UserLoginPasswordIncorrectException;
import com.onlinestore.onlinestore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity registration(@RequestBody UserEntity user) {
        try {
            userService.registration(user);
            return ResponseEntity.ok("User added");
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Fault");
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping(params = {"login", "password"})
    public ResponseEntity authorization(@RequestParam String login, @RequestParam String password, HttpServletResponse response) {
        try {
            userService.newCookie(response, login);
            return ResponseEntity.ok(userService.authorization(login, password));
        } catch (UserLoginPasswordIncorrectException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Password or login are incorrect");
        }
    }

}
