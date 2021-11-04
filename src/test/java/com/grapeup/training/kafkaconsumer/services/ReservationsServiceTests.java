package com.grapeup.training.kafkaconsumer.services;

import com.grapeup.training.kafkaconsumer.models.Reservation;
import com.grapeup.training.kafkaconsumer.repositories.ReservationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ReservationsServiceTests {
    private Reservation mockReservation;

    @MockBean
    private ReservationsRepository mockReservationsRepository;

    @Autowired
    private ReservationsService reservationsService;

    @BeforeEach
    public void setUp() {
        mockReservation =
                new Reservation(null, "John", "Smith", 8, LocalDate.of(2021, 8, 30), LocalDate.of(2021, 9, 5));
    }

    @Test
    public void create() {
        // Arrange
        Mockito.when(mockReservationsRepository.save(Mockito.any(Reservation.class)))
                .thenAnswer(i -> {
                    Reservation reservation = (Reservation) i.getArguments()[0];
                    reservation.setId(1L);
                    return reservation;
                });

        // Act
        Reservation reservation = reservationsService.create(mockReservation);

        // Assert
        assertEquals(mockReservation, reservation);
        Mockito.verify(mockReservationsRepository).save(mockReservation);
    }
}
