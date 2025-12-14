package com.lap.Order.Management.System.auth;

import com.lap.Order.Management.System.auth.dto.LoginRequest;
import com.lap.Order.Management.System.auth.dto.LoginResponse;
import com.lap.Order.Management.System.auth.dto.UserDto;
import com.lap.Order.Management.System.auth.user.User;
import com.lap.Order.Management.System.auth.user.UserRepo;
import com.lap.Order.Management.System.email.EmailService;
import com.lap.Order.Management.System.email.EmailTemplateService;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.util.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Recommended for logging
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    private final EmailTemplateService emailTemplateService;

    private final Map<String, OtpContext> otpStorage = new ConcurrentHashMap<>();
    private static final long OTP_VALID_DURATION = 5 * 60 * 1000;

    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        Optional<User> userOptional = userRepo.findByUsernameOrEmail(request.getUsernameOrEmail(), request.getUsernameOrEmail());

        if (userOptional.isEmpty()) {
            return buildErrorResponse("User not found");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return buildErrorResponse("Invalid Password");
        }

        if (user.getRole() == Role.ADMINISTRATEUR) {
            return handleAdminLogin(user);
        }

        return buildSuccessResponse(user, "Login Successful");
    }

    public LoginResponse verifyOtp(String identifier, String otpCode) {
        // 1. Resolve the User first (User might have sent Email OR Username)
        Optional<User> userOptional = userRepo.findByUsernameOrEmail(identifier, identifier);

        if (userOptional.isEmpty()) {
            return buildErrorResponse("User not found");
        }

        User user = userOptional.get();
        String username = user.getUsername(); // We use the specific username as the Map Key

        // 2. Check if OTP exists for this username
        if (!otpStorage.containsKey(username)) {
            return buildErrorResponse("No OTP request found or OTP expired");
        }

        OtpContext otpContext = otpStorage.get(username);

        // 3. Check Expiration
        if (System.currentTimeMillis() > otpContext.expiryTime) {
            otpStorage.remove(username);
            return buildErrorResponse("OTP has expired. Please login again.");
        }

        // 4. Validate Code
        if (otpContext.otpCode.equals(otpCode)) {
            otpStorage.remove(username); // Clear OTP after success
            return buildSuccessResponse(user, "OTP Verified. Login Successful.");
        } else {
            return buildErrorResponse("Invalid OTP");
        }
    }

    // --- Helper Methods ---

    private LoginResponse handleAdminLogin(User user) {
        // Generate Secure OTP
        SecureRandom secureRandom = new SecureRandom();
        String otp = String.valueOf(secureRandom.nextInt(900000) + 100000);

        // Store OTP with Expiration Time
        otpStorage.put(user.getUsername(), new OtpContext(otp, System.currentTimeMillis() + OTP_VALID_DURATION));

        // Send Email
        String emailBody = "Bonjour " + user.getUsername() + ",\n\n" +
                "Voici votre code de vérification (OTP) : " + otp + "\n\n" +
                "Ce code expire dans 5 minutes.\n" +
                "Pour des raisons de sécurité, ne partagez jamais ce code.\n\n" +
                "Cordialement,\n" +
                "L'équipe LAP System";

        // Wrap in try-catch so login doesn't crash if email server is down
        try {
            emailService.sendEmail(user.getEmail(), "Connexion Admin - Code de Vérification", emailBody);
        } catch (Exception e) {
            log.error("Failed to send OTP email", e);
            return buildErrorResponse("Error sending OTP email. Please try again later.");
        }

        LoginResponse response = new LoginResponse();
        response.setLoggedIn(false);
        response.setOtpRequired(true);
        response.setMessage("Vérification requise. Veuillez consulter votre boîte email.");
        response.setUserDto(null);
        return response;
    }

    private LoginResponse buildSuccessResponse(User user, String message) {
        LoginResponse response = new LoginResponse();
        response.setLoggedIn(true);
        response.setOtpRequired(false);
        response.setMessage(message);
        response.setUserDto(mapToDto(user));

        String subject = "Nouvelle connexion détectée – LAP";

        Map<String, String> data = new HashMap<>();
        data.put("username", user.getUsername());
        data.put("datetime", LocalDateTime.now().toString());
        data.put("companyName", "LAP");
        data.put("year", String.valueOf(Year.now().getValue()));

        String htmlBody = emailTemplateService.loadTemplate(
                "login-notification.html",
                data
        );

        notifyAdmins(subject, htmlBody);



        return response;
    }

    private LoginResponse buildErrorResponse(String message) {
        LoginResponse response = new LoginResponse();
        response.setLoggedIn(false);
        response.setOtpRequired(false);
        response.setMessage(message);
        return response;
    }

    private UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole());
        return userDto;
    }

    private void notifyAdmins(String subject, String body) {
        List<User> admins = userRepo.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMINISTRATEUR)
                .toList();
        for (User admin : admins) {
            emailService.sendHtmlEmail(admin.getEmail(), subject, body);
        }
    }

    // Inner class to hold OTP data
    private record OtpContext(String otpCode, long expiryTime) {}
}