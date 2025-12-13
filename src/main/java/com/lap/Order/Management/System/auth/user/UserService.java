package com.lap.Order.Management.System.auth.user;

import com.lap.Order.Management.System.auth.dto.NewUserDto;
import com.lap.Order.Management.System.auth.dto.NewUserResponse;
import com.lap.Order.Management.System.commande.Commande;
import com.lap.Order.Management.System.commande.CommandeService;
import com.lap.Order.Management.System.commande.dto.CommandeDto;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.tache.Task;
import com.lap.Order.Management.System.tache.TaskService;
import com.lap.Order.Management.System.util.PasswordEncoder;
import com.lap.Order.Management.System.zone.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired private UserRepo userRepo;
    @Autowired private ZoneService zoneService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private TaskService taskService;

    public NewUserResponse newUser(NewUserDto newUserDto) {

        NewUserResponse response = new NewUserResponse();

        if (newUserDto.getRole() == Role.ADMINISTRATEUR) {
            response.setCreated(false);
            response.setMessage("God Can't Create Himself Hhhhh");
            response.setNewUserDto(null);

            return response;
        }

        NewUserDto userDto = new NewUserDto();
        var encodedPassword = passwordEncoder.encode(newUserDto.getPassword());
        userDto.setPassword(encodedPassword);
        userDto.setEmail(newUserDto.getEmail());
        userDto.setRole(newUserDto.getRole());

        response.setCreated(true);
        response.setMessage("User Created");
        response.setNewUserDto(userDto);

        return response;

    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }

    public void createZone(String zone) {
        zoneService.create(zone);
    }

    public void deleteZone(Long id) {
        zoneService.delete(id);
    }


}
