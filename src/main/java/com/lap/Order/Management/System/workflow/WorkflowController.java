package com.lap.Order.Management.System.workflow;

import com.lap.Order.Management.System.tache.dto.AssignTaskDto;
import com.lap.Order.Management.System.util.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/workflow")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping("/assign")
    public ResponseEntity<Void> assignTask(@RequestBody AssignTaskDto request) {
        workflowService.assignTask(request.getCommandeId(), request.getAssigneeId(), request.getTaskType());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/task/{taskId}/complete")
    public ResponseEntity<Void> completeTask(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);
        workflowService.completeTask(taskId, fileName);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/task/{taskId}/complete-simple")
    public ResponseEntity<Void> completeTaskSimple(@PathVariable Long taskId) {
        workflowService.completeTaskSimple(taskId);
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