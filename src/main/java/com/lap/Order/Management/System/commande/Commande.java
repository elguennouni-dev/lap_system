package com.lap.Order.Management.System.commande;

import com.lap.Order.Management.System.commande.oneway.OnewayDetails;
import com.lap.Order.Management.System.commande.panneau.PanneauDetails;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.enums.TypeTravaux;
import com.lap.Order.Management.System.zone.Zone;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "commande")
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String nomPropriete;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Enumerated(EnumType.STRING)
    private TypeTravaux typeTravaux;

    @Enumerated(EnumType.STRING)
    private CommandeEtat etat;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "panneau_details_id")
    private PanneauDetails panneauDetails;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "oneway_details_id")
    private OnewayDetails onewayDetails;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateCreation;

}