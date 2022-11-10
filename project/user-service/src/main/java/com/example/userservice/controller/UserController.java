package com.example.userservice.controller;

import com.example.userservice.vo.Greeting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/")
public class UserController {

    private final Greeting greeting;

    public UserController(Greeting greeting) {
        this.greeting = greeting;
    }

    @GetMapping("/health_check")
    public String status() {
        return greeting.getMessage();
    }
}
