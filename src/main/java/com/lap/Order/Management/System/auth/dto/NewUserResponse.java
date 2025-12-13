package com.lap.Order.Management.System.auth.dto;

import lombok.Data;

@Data
public class NewUserResponse {

    private boolean created;
    private String message;
    private NewUserDto newUserDto;

}
