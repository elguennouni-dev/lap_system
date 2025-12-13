package com.lap.Order.Management.System.workflow;

import com.lap.Order.Management.System.auth.user.User;
import com.lap.Order.Management.System.auth.user.UserRepo;
import com.lap.Order.Management.System.commande.Commande;
import com.lap.Order.Management.System.commande.CommandeRepo;
import com.lap.Order.Management.System.email.EmailService;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.enums.Role;
import com.lap.Order.Management.System.enums.TaskStatus;
import com.lap.Order.Management.System.enums.TaskType;
import com.lap.Order.Management.System.tache.Task;
import com.lap.Order.Management.System.tache.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkflowService {

    @Autowired private TaskRepo taskRepo;
    @Autowired private CommandeRepo commandeRepo;
    @Autowired private UserRepo userRepo;
    @Autowired private EmailService emailService;

    @Transactional
    public void assignTask(Long commandeId, Long assigneeId, TaskType taskType) {
        Commande commande = commandeRepo.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        User assignee = userRepo.findById(assigneeId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (taskType == TaskType.DESIGN && commande.getEtat() != CommandeEtat.CREEE) {
            throw new RuntimeException("Design can only be assigned when Order is CREEE");
        }
        if (taskType == TaskType.IMPRESSION && commande.getEtat() != CommandeEtat.EN_IMPRESSION) {
            throw new RuntimeException("Impression can only be assigned when Design is Validated");
        }
        if (taskType == TaskType.LIVRAISON && commande.getEtat() != CommandeEtat.IMPRESSION_VALIDE) {
            throw new RuntimeException("Livraison can only be assigned when Impression is Validated");
        }

        Task task = new Task();
        task.setType(taskType);
        task.setStatus(TaskStatus.ASSIGNEE);
        task.setAssignee(assignee);
        task.setCommande(commande);
        taskRepo.save(task);

        updateCommandeStatusOnAssignment(commande, taskType);

        String subject = "🔔 Nouvelle Tâche : " + taskType;
        String body = "Bonjour " + assignee.getUsername() + ",\n\n" +
                "Une nouvelle tâche de type " + taskType + " vous a été assignée.\n" +
                "Commande ID : " + commandeId + "\n" +
                "Client : " + commande.getNomPropriete() + "\n\n" +
                "Merci de commencer le travail.";

        emailService.sendEmail(assignee.getEmail(), subject, body);
    }

    @Transactional
    public void completeTask(Long taskId, String fileUrl) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(TaskStatus.TERMINEE);
        task.setUploadFile(fileUrl);
        task.setCompletedAt(LocalDateTime.now());
        taskRepo.save(task);

        String subject = "✅ Tâche Terminée : " + task.getType();
        String body = "L'utilisateur " + task.getAssignee().getUsername() + " a terminé sa tâche.\n" +
                "Commande ID : " + task.getCommande().getId() + "\n" +
                "Type : " + task.getType() + "\n\n" +
                "Veuillez valider le travail sur le système.";

        notifyAdmins(subject, body);
    }

    @Transactional
    public void validateTask(Long taskId, boolean isApproved) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (isApproved) {
            task.setStatus(TaskStatus.VALIDEE);
            moveCommandeToNextStage(task.getCommande(), task.getType());

            emailService.sendEmail(task.getAssignee().getEmail(),
                    "🎉 Tâche Validée",
                    "Bravo ! Votre travail pour la commande #" + task.getCommande().getId() + " a été validé.");

        } else {
            task.setStatus(TaskStatus.REJETEE);

            emailService.sendEmail(task.getAssignee().getEmail(),
                    "⚠️ Tâche Rejetée",
                    "Votre travail pour la commande #" + task.getCommande().getId() + " a été rejeté.\n" +
                            "Veuillez contacter l'administrateur pour plus de détails.");
        }
        taskRepo.save(task);
    }

    @Transactional
    public void moveStock(Long commandeId) {
        Commande commande = commandeRepo.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande not found"));

        if(commande.getEtat() == CommandeEtat.LIVRAISON_VALIDE) {
            commande.setEtat(CommandeEtat.TERMINEE_STOCK);
            commandeRepo.save(commande);
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
            case DESIGN -> commande.setEtat(CommandeEtat.EN_IMPRESSION);
            case IMPRESSION -> commande.setEtat(CommandeEtat.IMPRESSION_VALIDE);
            case LIVRAISON -> commande.setEtat(CommandeEtat.LIVRAISON_VALIDE);
        }
        commandeRepo.save(commande);
    }

    private void notifyAdmins(String subject, String body) {
        List<User> admins = userRepo.findAll().stream()
                .filter(u -> u.getRole() == Role.ADMINISTRATEUR)
                .toList();

        for (User admin : admins) {
            emailService.sendEmail(admin.getEmail(), subject, body);
        }
    }
}