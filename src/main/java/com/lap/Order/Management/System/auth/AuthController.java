package com.lap.Order.Management.System.auth;

import com.lap.Order.Management.System.auth.dto.LoginRequest;
import com.lap.Order.Management.System.auth.dto.LoginResponse;
import com.lap.Order.Management.System.auth.dto.NewUserDto;
import com.lap.Order.Management.System.auth.user.User;
import com.lap.Order.Management.System.auth.user.UserRepo;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired private AuthService authService;

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepo userRepo;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponse> verifyOtp(@RequestParam String username, @RequestParam String otp) {
        return ResponseEntity.ok(authService.verifyOtp(username, otp));
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody NewUserDto newUserDto) {

        boolean emailAvailable =
                userRepo.findByEmail(newUserDto.getEmail()).isEmpty();

        if (!emailAvailable) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Email already exists");
        }

        User user = new User();
        user.setUsername(newUserDto.getUsername());
        user.setEmail(newUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        user.setRole(newUserDto.getRole());

        userRepo.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

}