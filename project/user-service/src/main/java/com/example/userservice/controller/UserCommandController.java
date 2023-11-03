package com.example.userservice.controller;

import com.example.userservice.dto.UserCommand;
import com.example.userservice.service.UserCommandService;
import com.example.userservice.utils.ModelMapperUtils;
import com.example.userservice.vo.RequestUser;
import com.example.userservice.vo.ResponseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserCommandController {
    private final UserCommandService userService;

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser requestUser) {

        UserCommand requestUserDto = ModelMapperUtils.modelMapper()
                .map(requestUser, UserCommand.class);
        UserCommand userDto = userService.createUser(requestUserDto);

        ResponseUser responseUser = ModelMapperUtils.modelMapper()
                .map(userDto, ResponseUser.class);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseUser);
    }

}
