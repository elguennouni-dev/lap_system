package com.lap.Order.Management.System.tache.dto;

import com.lap.Order.Management.System.enums.TaskType;
import lombok.Data;

@Data
public class AssignTaskDto {
    private Long commandeId;
    private Long assigneeId;
    private TaskType taskType;
}