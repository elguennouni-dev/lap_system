package com.lap.Order.Management.System.auth.dto;

import com.lap.Order.Management.System.enums.Role;
import lombok.Data;

@Data
public class NewUserDto {

    private String username;
    private String email;
    private String password;
    private Role role;

}
