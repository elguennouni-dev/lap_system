package com.lap.Order.Management.System.statistics;

import com.lap.Order.Management.System.commande.Commande;
import com.lap.Order.Management.System.commande.CommandeRepo;
import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.statistics.dto.DashboardStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final CommandeRepo commandeRepo;

    public DashboardStatsDto getDashboardStats() {
        List<Commande> allOrders = commandeRepo.findAll();

        long newOrders = allOrders.stream()
                .filter(o -> o.getEtat() == CommandeEtat.CREEE)
                .count();

        long inProgress = allOrders.stream()
                .filter(o -> isOrderInProgress(o.getEtat()))
                .count();

        long completed = allOrders.stream()
                .filter(o -> o.getEtat() == CommandeEtat.TERMINEE_STOCK)
                .count();

        // Calculate breakdown for every status (for charts)
        Map<String, Long> breakdown = new HashMap<>();
        for (CommandeEtat etat : CommandeEtat.values()) {
            long count = allOrders.stream().filter(o -> o.getEtat() == etat).count();
            breakdown.put(etat.name(), count);
        }

        return new DashboardStatsDto(newOrders, inProgress, completed, breakdown);
    }

    private boolean isOrderInProgress(CommandeEtat etat) {
        return etat == CommandeEtat.EN_DESIGN ||
                etat == CommandeEtat.EN_IMPRESSION ||
                etat == CommandeEtat.IMPRESSION_VALIDE ||
                etat == CommandeEtat.EN_LIVRAISON ||
                etat == CommandeEtat.LIVRAISON_VALIDE;
    }
}