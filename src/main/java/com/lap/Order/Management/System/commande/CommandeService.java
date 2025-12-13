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

    public void updateStatus(Long commandeId, CommandeEtat newStatus) {
        Commande commande = commandeRepo.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande not found"));
        commande.setEtat(newStatus);
        commandeRepo.save(commande);
    }
}