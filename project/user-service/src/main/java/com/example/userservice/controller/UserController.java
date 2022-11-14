package com.example.userservice.controller;

import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.service.UserService;
import com.example.userservice.utils.ModelMapperUtils;
import com.example.userservice.vo.Greeting;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


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
    public String status(HttpServletRequest request) {

        return String.format("It's working in User service on port %s", request.getServerPort());
    }
}
