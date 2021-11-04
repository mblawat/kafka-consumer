package com.grapeup.training.kafkaconsumer.services;

import com.grapeup.training.kafkaconsumer.models.Reservation;
import com.grapeup.training.kafkaconsumer.repositories.ReservationsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ReservationsService {
    private final Logger logger = LoggerFactory.getLogger(ReservationsService.class);
    private final ReservationsRepository reservationsRepository;

    public ReservationsService(ReservationsRepository reservationsRepository) {
        this.reservationsRepository = reservationsRepository;
    }

    public Reservation create(final Reservation reservation) {
        logger.debug("Adding new reservation: {}", reservation);
        reservationsRepository.save(reservation);
        return reservation;
    }
}
