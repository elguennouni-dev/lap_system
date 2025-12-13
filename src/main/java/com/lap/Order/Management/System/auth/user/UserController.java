package com.lap.Order.Management.System.auth.user;

import com.lap.Order.Management.System.auth.dto.NewUserDto;
import com.lap.Order.Management.System.auth.dto.NewUserResponse;
import com.lap.Order.Management.System.enums.Role;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/zone")
    public ResponseEntity<Void> createZone(@RequestParam String zone) {
        userService.createZone(zone);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/zone/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        userService.deleteZone(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<NewUserResponse> createUser(@RequestBody NewUserDto newUserDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.newUser(newUserDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }
}