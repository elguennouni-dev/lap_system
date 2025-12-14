package com.lap.Order.Management.System.tache.dto;

import com.lap.Order.Management.System.auth.dto.UserDto;
import com.lap.Order.Management.System.tache.Task;
import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String type;
    private String status;
    private UserDto assignee;
    private String uploadFile;

    public static TaskDto toDTO(Task task) {
        if (task == null) return null;

        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setType(task.getType() != null ? task.getType().name() : null);
        dto.setStatus(task.getStatus() != null ? task.getStatus().name() : null);
        dto.setUploadFile(task.getUploadFile());

        if (task.getAssignee() != null) {
            UserDto userDto = new UserDto();
            userDto.setId(task.getAssignee().getId());
            userDto.setUsername(task.getAssignee().getUsername());
            userDto.setRole(task.getAssignee().getRole());
            // Map other user fields if necessary
            dto.setAssignee(userDto);
        }

        return dto;
    }
}