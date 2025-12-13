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

    // Workflow Endpoints

    @PutMapping("/{id}/start-design")
    public ResponseEntity<CommandeDto> startDesign(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.startDesign(id));
    }

    @PutMapping("/{id}/validate-design")
    public ResponseEntity<CommandeDto> validateDesign(@PathVariable Long id, @RequestParam boolean accepted) {
        return ResponseEntity.ok(commandeService.validateDesign(id, accepted));
    }

    @PutMapping("/{id}/complete-impression")
    public ResponseEntity<CommandeDto> completeImpression(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.completeImpression(id));
    }

    @PutMapping("/{id}/start-livraison")
    public ResponseEntity<CommandeDto> startLivraison(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.startLivraison(id));
    }

    @PutMapping("/{id}/validate-livraison")
    public ResponseEntity<CommandeDto> validateLivraison(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.validateLivraison(id));
    }

    @PutMapping("/{id}/move-to-stock")
    public ResponseEntity<CommandeDto> moveToStock(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.moveToStock(id));
    }
}