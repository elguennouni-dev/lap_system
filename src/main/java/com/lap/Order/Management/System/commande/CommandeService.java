package com.lap.Order.Management.System.commande;

import com.lap.Order.Management.System.commande.dto.CommandeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    @Autowired private CommandeRepo commandeRepo;

    public List<CommandeDto> getAll() {
        return commandeRepo.findAll().stream()
                .map(CommandeDto::toDTO)
                .collect(Collectors.toList());
    }
    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepo.findById(id);
    }

    public CommandeDto getById(Long commandeId) {
        return commandeRepo.findById(commandeId).stream().map(CommandeDto::toDTO).collect(Collectors.toList());
    }




}
