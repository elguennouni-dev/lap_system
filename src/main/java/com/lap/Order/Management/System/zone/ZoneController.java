package com.lap.Order.Management.System.zone;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/zone")
@RequiredArgsConstructor
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping
    public ResponseEntity<List<Zone>> getAllZones() {
        return ResponseEntity.ok(zoneService.getAll());
    }

    @PostMapping
    public ResponseEntity<String> createZone(@RequestBody Map<String, String> request) {
        String zoneName = request.get("nom");
        if (zoneName == null || zoneName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Zone name is required");
        }
        zoneService.create(zoneName);
        return ResponseEntity.status(HttpStatus.CREATED).body("Zone created successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}