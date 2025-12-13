package com.lap.Order.Management.System.commande;

import com.lap.Order.Management.System.commande.dto.CommandeDao;
import com.lap.Order.Management.System.commande.dto.CommandeDto;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.enums.TypeTravaux;
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

    public CommandeDto create(CommandeDao commandeDao, String logoPath, String photoFacadePath) {
        Commande commande = new Commande();
        commande.setEtat(CommandeEtat.CREEE);
        commande.setZone(zoneRepo.findById(commandeDao.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zone not found")));
        commande.setTypeTravaux(commandeDao.getTypeTravaux());
        commande.setNomPropriete(commandeDao.getNomPropriete());

        if (commandeDao.getTypeTravaux() == TypeTravaux.PANNEAU) {
            if (commandeDao.getPanneauDetails() == null) {
                throw new RuntimeException("Panneau details are required for type PANNEAU");
            }
            if (logoPath != null) {
                commandeDao.getPanneauDetails().setLogoFile(logoPath);
            }
            commande.setPanneauDetails(commandeDao.getPanneauDetails());
        }
        else if (commandeDao.getTypeTravaux() == TypeTravaux.ONEWAY) {
            if (commandeDao.getOnewayDetails() == null) {
                throw new RuntimeException("Oneway details are required for type ONEWAY");
            }
            if (logoPath != null) {
                commandeDao.getOnewayDetails().setLogoFile(logoPath);
            }
            if (photoFacadePath != null) {
                commandeDao.getOnewayDetails().setPhotoFacadeFile(photoFacadePath);
            }
            commande.setOnewayDetails(commandeDao.getOnewayDetails());
        }

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
}