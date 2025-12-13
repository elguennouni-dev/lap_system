package com.lap.Order.Management.System.commande;

import com.lap.Order.Management.System.commande.dto.CommandeDao;
import com.lap.Order.Management.System.commande.dto.CommandeDto;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.zone.ZoneRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepo commandeRepo;

    @Autowired
    private ZoneRepo zoneRepo;

    public CommandeDto create(CommandeDao commandeDao) {
        Commande commande = new Commande();
        commande.setEtat(CommandeEtat.CREEE);
        commande.setZone(zoneRepo.findById(commandeDao.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found")));
        commande.setTypeTravaux(commandeDao.getTypeTravaux());
        commande.setNomPropriete(commandeDao.getNomPropriete());

        return CommandeDto.toDTO(commandeRepo.save(commande));
    }

    public List<CommandeDto> getAll() {
        return commandeRepo.findAll().stream()
                .map(CommandeDto::toDTO)
                .collect(Collectors.toList());
    }

    public CommandeDto getById(Long commandeId) {
        return commandeRepo.findById(commandeId)
                .map(CommandeDto::toDTO)
                .orElseThrow(() -> new RuntimeException("Commande not found"));
    }

    public void delete(Long commandeId) {
        if (!commandeRepo.existsById(commandeId)) {
            throw new RuntimeException("Commande not found");
        }
        commandeRepo.deleteById(commandeId);
    }

    // Workflow Methods

    public CommandeDto startDesign(Long commandeId) {
        Commande commande = findCommande(commandeId);
        if (commande.getEtat() != CommandeEtat.CREEE) {
            throw new RuntimeException("Invalid state transition. Order must be CREEE.");
        }
        commande.setEtat(CommandeEtat.EN_DESIGN);
        return CommandeDto.toDTO(commandeRepo.save(commande));
    }

    public CommandeDto validateDesign(Long commandeId, boolean accepted) {
        Commande commande = findCommande(commandeId);
        if (commande.getEtat() != CommandeEtat.EN_DESIGN) {
            throw new RuntimeException("Invalid state transition. Order must be EN_DESIGN.");
        }
        if (accepted) {
            commande.setEtat(CommandeEtat.EN_IMPRESSION);
        } else {
            // Logic for rejection (e.g., stay in EN_DESIGN or revert)
            throw new RuntimeException("Design rejected");
        }
        return CommandeDto.toDTO(commandeRepo.save(commande));
    }

    public CommandeDto completeImpression(Long commandeId) {
        Commande commande = findCommande(commandeId);
        if (commande.getEtat() != CommandeEtat.EN_IMPRESSION) {
            throw new RuntimeException("Invalid state transition. Order must be EN_IMPRESSION.");
        }
        commande.setEtat(CommandeEtat.IMPRESSION_VALIDE);
        return CommandeDto.toDTO(commandeRepo.save(commande));
    }

    public CommandeDto startLivraison(Long commandeId) {
        Commande commande = findCommande(commandeId);
        if (commande.getEtat() != CommandeEtat.IMPRESSION_VALIDE) {
            throw new RuntimeException("Invalid state transition. Order must be IMPRESSION_VALIDE.");
        }
        commande.setEtat(CommandeEtat.EN_LIVRAISON);
        return CommandeDto.toDTO(commandeRepo.save(commande));
    }

    public CommandeDto validateLivraison(Long commandeId) {
        Commande commande = findCommande(commandeId);
        if (commande.getEtat() != CommandeEtat.EN_LIVRAISON) {
            throw new RuntimeException("Invalid state transition. Order must be EN_LIVRAISON.");
        }
        commande.setEtat(CommandeEtat.LIVRAISON_VALIDE);
        return CommandeDto.toDTO(commandeRepo.save(commande));
    }

    public CommandeDto moveToStock(Long commandeId) {
        Commande commande = findCommande(commandeId);
        if (commande.getEtat() != CommandeEtat.LIVRAISON_VALIDE) {
            throw new RuntimeException("Invalid state transition. Order must be LIVRAISON_VALIDE.");
        }
        commande.setEtat(CommandeEtat.TERMINEE_STOCK);
        return CommandeDto.toDTO(commandeRepo.save(commande));
    }

    private Commande findCommande(Long id) {
        return commandeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande not found"));
    }
}