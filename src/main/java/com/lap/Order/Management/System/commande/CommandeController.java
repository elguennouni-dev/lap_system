package com.lap.Order.Management.System.commande;

import com.lap.Order.Management.System.commande.dto.CommandeDao;
import com.lap.Order.Management.System.commande.dto.CommandeDto;
import com.lap.Order.Management.System.util.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<CommandeDto> createCommande(
            @RequestPart("data") CommandeDao commandeDao,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "photoFacade", required = false) MultipartFile photoFacade) {

        String logoPath = null;
        if (logo != null && !logo.isEmpty()) {
            logoPath = fileStorageService.storeFile(logo);
        }

        // Handle Facade Photo Upload
        String photoFacadePath = null;
        if (photoFacade != null && !photoFacade.isEmpty()) {
            photoFacadePath = fileStorageService.storeFile(photoFacade);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commandeService.create(commandeDao, logoPath, photoFacadePath));
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