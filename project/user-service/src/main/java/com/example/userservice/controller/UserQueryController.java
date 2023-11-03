package com.example.userservice.controller;

import com.example.userservice.service.UserQueryService;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserQueryController {

    private final UserQueryService userService;
    private final Environment env;

    @GetMapping("/users/{userId}")
    public ResponseEntity<ResponseUser> user(@PathVariable String userId) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserByUserId(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<ResponseUser>> users() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getUserByAll());
    }

    @GetMapping("/health_check")
    @Timed(value = "users.status", longTask = true)
    public String status(HttpServletRequest request) {

        return String.format("port(local.server.port)=" + env.getProperty("local.server.port") +
                ", port(server.port)=" + env.getProperty("server.port") +
                ", token=" + env.getProperty("token.secret") +
                ", expiration=" + env.getProperty("token.expiration_time")) ;
    }

    @GetMapping("/welcome")
    @Timed(value = "users.welcome", longTask = true)
    public String hello() {
        return "hello";
    }
}
