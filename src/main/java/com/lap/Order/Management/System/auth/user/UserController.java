package com.lap.Order.Management.System.auth.user;

import com.lap.Order.Management.System.auth.dto.NewUserDto;
import com.lap.Order.Management.System.auth.dto.NewUserResponse;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired UserService userService;

    @PostMapping("/zone")
    public void createZone(@RequestParam String zone) {
        userService.createZone(zone);
    }

    @DeleteMapping("/zone/{id}")
    public void deleteZone(@RequestParam Long id) {
        userService.deleteZone(id);
    }

    @PostMapping("/createuser")
    public ResponseEntity<NewUserResponse> createUserByAdmin(@RequestBody NewUserDto newUserDto) {
        return ResponseEntity.ok(userService.newUser(newUserDto));
    }


    @DeleteMapping("/user{id}")
    public void deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
    }


    @PostMapping("/task/assignee")
    public ResponseEntity<?> assigneeTask(@RequestBody AssigneeTask assigneeTask) {
        return ResponseEntity.ok(userService.assigneTask(assigneeTask.getUserId(), assigneeTask.getCommandId()));
    }


}

@Data
class AssigneeTask {
    Long commandId;
    Long userId;
}