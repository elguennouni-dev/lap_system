package com.lap.Order.Management.System.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    private String message;
    private Long orderId;
    private String newStatus;
    private String timestamp;
}