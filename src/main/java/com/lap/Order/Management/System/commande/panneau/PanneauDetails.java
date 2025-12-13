package com.lap.Order.Management.System.commande.panneau;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "panneau_details")
public class PanneauDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hauteurCm;
    private int largeurCm;

    private boolean contenuPanneau;
    private boolean logoNomRequis;

    @Column(length = 500)
    private String logoFile;
    private String nomSaisi;

    @Column(columnDefinition = "TEXT")
    private String autresDetails;

}