package com.example.userservice.vo;


import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestUser {

    @NotNull(message = "Email cannot be null")
    @Size(min = 2, message = "Email not to be less than two characters")
    private String email;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "name not to be less than two characters")
    private String name;

    @NotNull(message = "Name cannot be null")
    @Size(min = 2, message = "name not to be less than two characters")
    private String pwd;
}
