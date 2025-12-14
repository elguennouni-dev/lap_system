package com.lap.Order.Management.System.zone;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ZoneService {

    private final ZoneRepo zoneRepo;

    public List<Zone> getAll() {
        return zoneRepo.findAll();
    }

    public void create(String zoneName) {
        Zone zone = new Zone();
        zone.setNom(zoneName);
        zoneRepo.save(zone);
    }

    public void delete(Long id) {
        zoneRepo.deleteById(id);
    }
}