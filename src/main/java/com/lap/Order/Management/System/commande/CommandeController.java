package com.lap.Order.Management.System.commande;

import com.lap.Order.Management.System.commande.dto.CommandeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/commande")
public class CommandeController {

    @Autowired private CommandeService commandeService;

    @GetMapping()
    public ResponseEntity<List<CommandeDto>> getAllCommande() {
        return ResponseEntity.ok(commandeService.getAll());
    }

    @GetMapping("{commandId}")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long commandeId) {
        return ResponseEntity.ok(commandeService.getById(commandeId));
    }

}
