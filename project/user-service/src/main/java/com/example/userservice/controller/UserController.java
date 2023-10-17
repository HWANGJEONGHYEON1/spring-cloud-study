package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.utils.ModelMapperUtils;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser) {

        UserDto requestUserDto = ModelMapperUtils.modelMapper()
                .map(requestUser, UserDto.class);
        UserDto userDto = userService.createUser(requestUserDto);

        ResponseUser responseUser = ModelMapperUtils.modelMapper()
                .map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseUser);
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
