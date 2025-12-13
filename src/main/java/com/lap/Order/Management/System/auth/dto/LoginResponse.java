package com.lap.Order.Management.System.auth.dto;

import lombok.Data;

@Data
public class LoginResponse {

    private boolean loggedIn;
    private boolean otpRequired;
    private String message;
    private UserDto userDto;

}
