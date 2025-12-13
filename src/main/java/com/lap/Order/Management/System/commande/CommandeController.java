package com.lap.Order.Management.System.commande;

import com.lap.Order.Management.System.commande.dto.CommandeDao;
import com.lap.Order.Management.System.commande.dto.CommandeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @PostMapping
    public ResponseEntity<CommandeDto> createCommande(@RequestBody CommandeDao commandeDao) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commandeService.create(commandeDao));
    }

    @GetMapping
    public ResponseEntity<List<CommandeDto>> getAllCommande() {
        return ResponseEntity.ok(commandeService.getAll());
    }

    @GetMapping("/{commandeId}")
    public ResponseEntity<CommandeDto> getCommandeById(@PathVariable Long commandeId) {
        return ResponseEntity.ok(commandeService.getById(commandeId));
    }

    @DeleteMapping("/{commandeId}")
    public ResponseEntity<Void> deleteCommandeById(@PathVariable Long commandeId) {
        commandeService.delete(commandeId);
        return ResponseEntity.ok().build();
    }
}