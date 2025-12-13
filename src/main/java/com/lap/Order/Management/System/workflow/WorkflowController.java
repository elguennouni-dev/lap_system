package com.lap.Order.Management.System.workflow;

import com.lap.Order.Management.System.tache.dto.AssignTaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @PostMapping("/assign")
    public ResponseEntity<Void> assignTask(@RequestBody AssignTaskDto request) {
        workflowService.assignTask(request.getCommandeId(), request.getAssigneeId(), request.getTaskType());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/task/{taskId}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable Long taskId, @RequestParam String fileUrl) {
        workflowService.completeTask(taskId, fileUrl);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/task/{taskId}/validate")
    public ResponseEntity<Void> validateTask(@PathVariable Long taskId, @RequestParam boolean approved) {
        workflowService.validateTask(taskId, approved);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/commande/{commandeId}/move-to-stock")
    public ResponseEntity<Void> moveToStock(@PathVariable Long commandeId) {
        workflowService.moveStock(commandeId);
        return ResponseEntity.ok().build();
    }
}