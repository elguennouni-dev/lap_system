package com.lap.Order.Management.System.zone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ZoneService {

    @Autowired ZoneRepo zoneRepo;

    public void create(String zoneName) {
        Zone zone = new Zone();
        zone.setNom(zoneName);
        zoneRepo.save(zone);
    }

    public void delete(Long id) {
        zoneRepo.deleteById(id);
    }

}
