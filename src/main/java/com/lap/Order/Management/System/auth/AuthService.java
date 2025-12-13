package com.lap.Order.Management.System.auth;

import com.lap.Order.Management.System.auth.dto.LoginRequest;
import com.lap.Order.Management.System.auth.dto.LoginResponse;
import com.lap.Order.Management.System.auth.dto.UserDto;
import com.lap.Order.Management.System.auth.user.User;
import com.lap.Order.Management.System.auth.user.UserRepo;
import com.lap.Order.Management.System.auth.user.UserService;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired private UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        Optional<User> userOptional = userRepo.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail());

        if (userOptional.isEmpty()) {
            response.setLoggedIn(false);
            response.setMessage("User not found");
            return response;
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            response.setLoggedIn(false);
            response.setMessage("Invalid Password");
            return response;
        }

        if (user.getRole() == Role.ADMINISTRATEUR) {
            // OTP

            response.setLoggedIn(false);
            response.setOtpRequired(true);
            response.setMessage("OTP Verification Required");

            response.setUserDto(null);
        }else {
            response.setLoggedIn(true);
            response.setOtpRequired(false);
            response.setMessage("Login Successful");

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setRole(user.getRole());

            response.setUserDto(userDto);

        }

        return response;


    }

}
