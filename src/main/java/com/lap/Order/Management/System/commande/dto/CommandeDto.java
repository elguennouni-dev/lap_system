package com.lap.Order.Management.System.commande.dto;

import com.lap.Order.Management.System.commande.Commande;
import com.lap.Order.Management.System.commande.oneway.OnewayDetails;
import com.lap.Order.Management.System.commande.panneau.PanneauDetails;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.enums.TypeTravaux;
import com.lap.Order.Management.System.tache.dto.TaskDto; // Import the new TaskDto
import com.lap.Order.Management.System.zone.Zone;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommandeDto {

    private Long id;
    private String nomPropriete;
    private CommandeEtat etat;
    private TypeTravaux typeTravaux;
    private Zone zone;
    private PanneauDetails panneauDetails;
    private OnewayDetails onewayDetails;
    private LocalDateTime createdDate;

    // The list that the frontend is looking for
    private List<TaskDto> tasks = new ArrayList<>();

    public static CommandeDto toDTO(Commande commande) {
        if (commande == null) {
            return null;
        }

        CommandeDto dto = new CommandeDto();
        dto.setId(commande.getId());
        dto.setEtat(commande.getEtat());
        dto.setNomPropriete(commande.getNomPropriete());
        dto.setTypeTravaux(commande.getTypeTravaux());
        dto.setZone(commande.getZone());
        dto.setPanneauDetails(commande.getPanneauDetails());
        dto.setOnewayDetails(commande.getOnewayDetails());
        dto.setCreatedDate(commande.getDateCreation());

        // FIX: Map the tasks from Entity to DTO list
        if (commande.getTasks() != null && !commande.getTasks().isEmpty()) {
            dto.setTasks(commande.getTasks().stream()
                    .map(TaskDto::toDTO)
                    .collect(Collectors.toList()));
        }

        return dto;
    }
}