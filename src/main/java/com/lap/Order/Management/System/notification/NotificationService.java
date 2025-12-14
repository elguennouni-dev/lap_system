package com.lap.Order.Management.System.notification;

import com.lap.Order.Management.System.enums.CommandeEtat;
import com.lap.Order.Management.System.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    private static final String GENERAL_ORDER_TOPIC = "/topic/orders/updates";

    public void notifyOrderStatusChange(Long orderId, String nomPropriete, CommandeEtat oldStatus, CommandeEtat newStatus) {

        String message = String.format(
                "Commande #%d (%s) est passée de %s à %s.",
                orderId, nomPropriete, oldStatus.name(), newStatus.name()
        );

        NotificationDto notification = new NotificationDto(
                message,
                orderId,
                newStatus.name(),
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );

        messagingTemplate.convertAndSend(GENERAL_ORDER_TOPIC, notification);

        System.out.println("WebSocket Notification Sent: " + notification.getMessage());
    }
}