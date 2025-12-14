package com.lap.Order.Management.System.statistics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardStatsDto {
    private long newOrders;
    private long inProgress;
    private long completed;
    private Map<String, Long> breakdown;
}