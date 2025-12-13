package com.lap.Order.Management.System.auth.user;

import com.lap.Order.Management.System.auth.dto.NewUserDto;
import com.lap.Order.Management.System.auth.dto.NewUserResponse;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.tache.TaskService;
import com.lap.Order.Management.System.util.PasswordEncoder;
import com.lap.Order.Management.System.zone.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ZoneService zoneService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public NewUserResponse newUser(NewUserDto newUserDto) {
        NewUserResponse response = new NewUserResponse();

//        if (newUserDto.getRole() == Role.ADMINISTRATEUR) {
//            response.setCreated(false);
//            response.setMessage("God Can't Create Himself Hhhhh");
//            response.setNewUserDto(null);
//            return response;
//        }

//        if (userRepo.findByEmail(newUserDto.getEmail()).isPresent()) {
//            response.setCreated(false);
//            response.setMessage("User with this email already exists");
//            response.setNewUserDto(null);
//            return response;
//        }

        if (userRepo.findByEmail(newUserDto.getEmail()).isPresent() ||
                userRepo.findByUsername(newUserDto.getUsername()).isPresent()) {

            response.setCreated(false);
            response.setMessage("User already exists (Email or Username)");
            response.setNewUserDto(null);
            return response;
        }

        User user = new User();
        user.setEmail(newUserDto.getEmail());
        user.setUsername(newUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(newUserDto.getPassword()));
        user.setRole(newUserDto.getRole());

        userRepo.save(user);

        response.setCreated(true);
        response.setMessage("User Created");
        response.setNewUserDto(newUserDto);

        return response;
    }

    public void deleteUser(Long id) {
        if (userRepo.existsById(id)) {
            userRepo.deleteById(id);
        }
    }

    public void createZone(String zone) {
        zoneService.create(zone);
    }

    public void deleteZone(Long id) {
        zoneService.delete(id);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public List<User> getUsersByRole(Role role) {
        return userRepo.findAll().stream()
                .filter(user -> user.getRole() == role)
                .collect(Collectors.toList());
    }
}
