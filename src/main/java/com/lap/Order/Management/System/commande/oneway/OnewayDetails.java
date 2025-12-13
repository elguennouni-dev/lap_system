package com.lap.Order.Management.System.commande.oneway;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "oneway_details")
public class OnewayDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean manuscriptUpload;

    @Column(columnDefinition = "TEXT")
    private String manuscriptText;

    @Column(length = 100)
    private String photoFacadeFile;

    private boolean logoNomRequis;

    @Column(length = 100)
    private String logoFile;

    private String nomSaisi;

    @Column(columnDefinition = "TEXT")
    private String autreDetails;

}