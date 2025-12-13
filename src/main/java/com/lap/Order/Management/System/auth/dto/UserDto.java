package com.lap.Order.Management.System.auth.dto;

import com.lap.Order.Management.System.enums.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private Role role;

}
