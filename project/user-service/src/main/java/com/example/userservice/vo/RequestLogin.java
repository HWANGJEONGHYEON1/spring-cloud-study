package com.example.userservice.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestLogin {
    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;
}
