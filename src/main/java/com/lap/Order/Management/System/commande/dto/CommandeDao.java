package com.lap.Order.Management.System.commande.dto;

import com.lap.Order.Management.System.commande.oneway.OnewayDetails;
import com.lap.Order.Management.System.commande.panneau.PanneauDetails;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.enums.TypeTravaux;
import lombok.Data;

@Data
public class CommandeDao {
    private String nomPropriete;
    private Long zoneId;
    private TypeTravaux typeTravaux;
    private CommandeEtat etat;

    private PanneauDetails panneauDetails;
    private OnewayDetails onewayDetails;


}