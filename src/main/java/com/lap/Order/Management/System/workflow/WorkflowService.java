package com.lap.Order.Management.System.workflow;

import com.lap.Order.Management.System.auth.user.User;
import com.lap.Order.Management.System.auth.user.UserRepo;
import com.lap.Order.Management.System.commande.Commande;
import com.lap.Order.Management.System.commande.CommandeRepo;
import com.lap.Order.Management.System.email.EmailService;
import com.lap.Order.Management.System.email.EmailTemplateService;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.enums.TaskStatus;
import com.lap.Order.Management.System.enums.TaskType;
import com.lap.Order.Management.System.notification.NotificationService;
import com.lap.Order.Management.System.tache.Task;
import com.lap.Order.Management.System.tache.TaskRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkflowService {

    private final TaskRepo taskRepo;
    private final CommandeRepo commandeRepo;
    private final UserRepo userRepo;
    private final EmailService emailService;
    private final EmailTemplateService emailTemplateService;
    private final NotificationService notificationService;

    @Transactional
    public void assignTask(Long commandeId, Long assigneeId, TaskType taskType) {
        Commande commande = commandeRepo.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        User assignee = userRepo.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (taskType == TaskType.DESIGN && commande.getEtat() != CommandeEtat.CREEE) {
            throw new RuntimeException("Design can only be assigned when Order is CREEE");
        }
        if (taskType == TaskType.IMPRESSION && commande.getEtat() != CommandeEtat.IMPRESSION_VALIDE) {
            throw new RuntimeException("Impression can only be assigned when Design is Validated");
        }
        if (taskType == TaskType.LIVRAISON && commande.getEtat() != CommandeEtat.LIVRAISON_VALIDE) {
            throw new RuntimeException("Livraison can only be assigned when Impression is Validated");
        }

        Task task = new Task();
        task.setType(taskType);
        task.setStatus(TaskStatus.ASSIGNEE);
        task.setAssignee(assignee);
        task.setCommande(commande);
        taskRepo.save(task);

        CommandeEtat oldStatus = commande.getEtat();
        updateCommandeStatusOnAssignment(commande, taskType);
        CommandeEtat newStatus = commande.getEtat();

        notificationService.notifyOrderStatusChange(
                commande.getId(),
                commande.getNomPropriete(),
                oldStatus,
                newStatus
        );

        String subject = "Nouvelle Tâche : " + taskType;
        String htmlBody = emailTemplateService.loadTemplate(
                "task-notification.html",
                Map.of(
                        "assigneeName", assignee.getUsername(),
                        "taskType", taskType.toString(),
                        "commandeId", commandeId.toString(),
                        "clientName", commande.getNomPropriete()
                )
        );

        emailService.sendHtmlEmail(assignee.getEmail(), subject, htmlBody);
    }

    @Transactional
    public void completeTask(Long taskId, String fileUrl) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.TERMINEE);
        task.setUploadFile(fileUrl);
        task.setCompletedAt(LocalDateTime.now());
        taskRepo.save(task);

        String subject = "Tâche Terminée : " + task.getType();
        String htmlBody = emailTemplateService.loadTemplate(
                "task-completed.html",
                Map.of(
                        "assigneeName", task.getAssignee().getUsername(),
                        "taskType", task.getType().toString(),
                        "commandeId", task.getCommande().getId().toString(),
                        "clientName", task.getCommande().getNomPropriete()
                )
        );

        notifyAdminsHtml(subject, htmlBody);
    }

    @Transactional
    public void completeTaskSimple(Long taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (task.getType() == TaskType.DESIGN) {
            throw new RuntimeException("Design tasks require file upload.");
        }

        task.setStatus(TaskStatus.TERMINEE);
        task.setCompletedAt(LocalDateTime.now());
        taskRepo.save(task);

        String subject = "Tâche Terminée : " + task.getType();
        String htmlBody = emailTemplateService.loadTemplate(
                "task-completed.html",
                Map.of(
                        "assigneeName", task.getAssignee().getUsername(),
                        "taskType", task.getType().toString(),
                        "commandeId", task.getCommande().getId().toString(),
                        "clientName", task.getCommande().getNomPropriete()
                )
        );

        notifyAdminsHtml(subject, htmlBody);
    }


    @Transactional
    public void validateTask(Long taskId, boolean isApproved) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Commande commande = task.getCommande();
        CommandeEtat oldStatus = commande.getEtat();

        if (isApproved) {
            task.setStatus(TaskStatus.VALIDEE);
            moveCommandeToNextStage(commande, task.getType());

            String subject = "Tâche Validée : " + task.getType();
            String htmlBody = emailTemplateService.loadTemplate(
                    "task-validated.html",
                    Map.of(
                            "assigneeName", task.getAssignee().getUsername(),
                            "taskType", task.getType().toString(),
                            "commandeId", commande.getId().toString(),
                            "clientName", commande.getNomPropriete()
                    )
            );

            emailService.sendHtmlEmail(task.getAssignee().getEmail(), subject, htmlBody);

            notificationService.notifyOrderStatusChange(
                    commande.getId(),
                    commande.getNomPropriete(),
                    oldStatus,
                    commande.getEtat()
            );

        } else {
            task.setStatus(TaskStatus.REJETEE);

            String subject = "Tâche Rejetée : " + task.getType();
            String htmlBody = emailTemplateService.loadTemplate(
                    "task-rejected.html",
                    Map.of(
                            "assigneeName", task.getAssignee().getUsername(),
                            "taskType", task.getType().toString(),
                            "commandeId", commande.getId().toString(),
                            "clientName", commande.getNomPropriete()
                    )
            );

            emailService.sendHtmlEmail(task.getAssignee().getEmail(), subject, htmlBody);
        }
        taskRepo.save(task);
    }

    @Transactional
    public void moveStock(Long commandeId) {
        Commande commande = commandeRepo.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        CommandeEtat oldStatus = commande.getEtat();

        if(commande.getEtat() == CommandeEtat.LIVRAISON_VALIDE) {
            commande.setEtat(CommandeEtat.TERMINEE_STOCK);
            commandeRepo.save(commande);

            notificationService.notifyOrderStatusChange(
                    commande.getId(),
                    commande.getNomPropriete(),
                    oldStatus,
                    commande.getEtat()
            );

        } else {
            throw new RuntimeException("Order must be LIVRAISON_VALIDE to move to stock");
        }
    }

    private void updateCommandeStatusOnAssignment(Commande commande, TaskType type) {
        switch (type) {
            case DESIGN -> commande.setEtat(CommandeEtat.EN_DESIGN);
            case IMPRESSION -> commande.setEtat(CommandeEtat.EN_IMPRESSION);
            case LIVRAISON -> commande.setEtat(CommandeEtat.EN_LIVRAISON);
        }
        commandeRepo.save(commande);
    }

    private void moveCommandeToNextStage(Commande commande, TaskType type) {
        switch (type) {
            case DESIGN -> commande.setEtat(CommandeEtat.IMPRESSION_VALIDE);
            case IMPRESSION -> commande.setEtat(CommandeEtat.LIVRAISON_VALIDE);
            case LIVRAISON -> commande.setEtat(CommandeEtat.LIVRAISON_VALIDE);
        }
        commandeRepo.save(commande);
    }

    private void notifyAdminsHtml(String subject, String htmlBody) {
        List<User> admins = userRepo.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMINISTRATEUR)
                .collect(Collectors.toList());

        for (User admin : admins) {
            emailService.sendHtmlEmail(admin.getEmail(), subject, htmlBody);
        }
    }
}