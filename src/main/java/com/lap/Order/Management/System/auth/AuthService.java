package com.lap.Order.Management.System.auth;

import com.lap.Order.Management.System.auth.dto.LoginRequest;
import com.lap.Order.Management.System.auth.dto.LoginResponse;
import com.lap.Order.Management.System.auth.dto.UserDto;
import com.lap.Order.Management.System.auth.user.User;
import com.lap.Order.Management.System.auth.user.UserRepo;
import com.lap.Order.Management.System.auth.user.UserService;
import com.lap.Order.Management.System.email.EmailService;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired private EmailService emailService;

    private final Map<String, String> otpStorage = new ConcurrentHashMap<>();

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
            String otp = String.valueOf(new Random().nextInt(900000) + 100000);

            otpStorage.put(user.getUsername(), otp);

            String emailBody = "Bonjour Admin,\n\n" +
                    "Voici votre code de vérification (OTP) : " + otp + "\n\n" +
                    "Pour des raisons de sécurité, ne partagez jamais ce code.\n\n" +
                    "Cordialement,\n" +
                    "L'équipe LAP System";

            emailService.sendEmail(user.getEmail(), "Connexion Admin - Code de Vérification", emailBody);

            response.setLoggedIn(false);
            response.setOtpRequired(true);
            response.setMessage("Vérification requise. Veuillez consulter votre boîte email pour le code OTP.");
            response.setUserDto(null);

            return response;
        }

        response.setLoggedIn(true);
        response.setOtpRequired(false);
        response.setMessage("Login Successful");

        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());

        response.setUserDto(userDto);

        return response;
    }


    public LoginResponse verifyOtp(String username, String otpCode) {
        LoginResponse response = new LoginResponse();

        if (otpStorage.containsKey(username) && otpStorage.get(username).equals(otpCode)) {
            otpStorage.remove(username);

            User user = userRepo.findByUsername(username).orElseThrow();

            response.setLoggedIn(true);
            response.setOtpRequired(false);
            response.setMessage("OTP Verified. Login Successful.");

            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDto.setRole(user.getRole());
            response.setUserDto(userDto);

        } else {
            response.setLoggedIn(false);
            response.setMessage("Invalid or Expired OTP");
        }
        return response;
    }

}