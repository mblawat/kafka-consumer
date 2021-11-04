package com.grapeup.training.kafkaconsumer.consumers;

import com.grapeup.training.kafkaconsumer.models.Reservation;
import com.grapeup.training.kafkaconsumer.services.ReservationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@KafkaListener(topics = "com.grapeup.training.createReservation")
public class KafkaReservationConsumer {
    private final Logger logger = LoggerFactory.getLogger(KafkaReservationConsumer.class);
    private ReservationsService reservationsService;

    public KafkaReservationConsumer(ReservationsService reservationsService) {
        this.reservationsService = reservationsService;
    }

    @KafkaHandler
    void listen(@Payload Reservation reservation) {
        logger.debug("Received Reservation to be created: {}", reservation);
        reservationsService.create(reservation);
    }

}

