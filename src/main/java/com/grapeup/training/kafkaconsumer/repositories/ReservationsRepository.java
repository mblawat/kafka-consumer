package com.grapeup.training.kafkaconsumer.repositories;

import com.grapeup.training.kafkaconsumer.models.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface ReservationsRepository extends CrudRepository<Reservation, Long> {
}
