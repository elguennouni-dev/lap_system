package com.lap.Order.Management.System.commande.dto;

import com.lap.Order.Management.System.commande.Commande;
import com.lap.Order.Management.System.enums.CommandeEtat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommandeDto {

    private Long id;
    private String nomPropriete;
    private CommandeEtat etat;
    private LocalDateTime dateCreation;


    public static CommandeDto toDTO(Commande commande) {

        if (commande instanceof Commande) {
            CommandeDto dto = new CommandeDto();
            dto.setId(commande.getId());
            dto.setEtat(commande.getEtat());
            dto.setNomPropriete(commande.getNomPropriete());
            dto.setDateCreation(commande.getDateCreation());
        }

        return null;
    }


}
