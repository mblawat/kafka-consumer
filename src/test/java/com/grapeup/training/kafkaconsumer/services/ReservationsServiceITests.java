package com.grapeup.training.kafkaconsumer.services;

import com.grapeup.training.kafkaconsumer.models.Reservation;
import com.grapeup.training.kafkaconsumer.repositories.ReservationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ReservationsServiceITests {
    private Reservation mockReservation;

    @Autowired
    private ReservationsRepository reservationsRepository;

    @Autowired
    private ReservationsService reservationsService;

    @BeforeEach
    public void setUp() {
        reservationsRepository.deleteAll();
        mockReservation =
                new Reservation(null, "John", "Smith", 8, LocalDate.of(2021, 8, 30), LocalDate.of(2021, 9, 5));
    }

    @Test
    public void create() {
        // Act
        Reservation createdReservation = reservationsService.create(mockReservation);

        // Assert
        assertEquals(mockReservation, createdReservation);
        assertNotNull(createdReservation.getId());
    }

}
