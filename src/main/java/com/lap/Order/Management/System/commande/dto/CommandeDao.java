package com.lap.Order.Management.System.commande.dto;

import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.enums.TypeTravaux;
import lombok.Data;

@Data
public class CommandeDao {
    private String nomPropriete;
    private Long zoneId;
    private TypeTravaux typeTravaux;
    private CommandeEtat etat;



}