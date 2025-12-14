package com.lap.Order.Management.System.statistics;

import com.lap.Order.Management.System.statistics.dto.DashboardStatsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDto> getDashboardStats() {
        return ResponseEntity.ok(statisticsService.getDashboardStats());
    }
}