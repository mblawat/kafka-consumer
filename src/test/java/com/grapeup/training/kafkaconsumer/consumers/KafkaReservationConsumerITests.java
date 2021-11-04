package com.grapeup.training.kafkaconsumer.consumers;

import com.grapeup.training.kafkaconsumer.models.Reservation;
import com.grapeup.training.kafkaconsumer.repositories.ReservationsRepository;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@EmbeddedKafka
@SpringBootTest(properties = "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KafkaReservationConsumerITests {

    private Producer<String, Reservation> producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    private ReservationsRepository reservationsRepository;

    @SpyBean
    private KafkaReservationConsumer kafkaReservationConsumer;

    @BeforeAll
    void setUp() {
        Map<String, Object> configs = new HashMap<>(KafkaTestUtils.producerProps(embeddedKafkaBroker));
        producer = new DefaultKafkaProducerFactory<>(configs, new StringSerializer(),
                new JsonSerializer<Reservation>()).createProducer();
    }

    @BeforeEach
    void cleanUp() {
        reservationsRepository.deleteAll();
    }

    @AfterAll
    void tearDown() {
        producer.close();
    }

    @Test
    public void listen() {
        // Arrange
        Reservation reservation =
                new Reservation(1L, "John", "Smith", 8, LocalDate.of(2021, 8, 30), LocalDate.of(2021, 9, 5));

        // Act
        kafkaReservationConsumer.listen(reservation);

        // Assert
        Optional<Reservation> savedReservation = reservationsRepository.findById(1L);
        assertTrue(savedReservation.isPresent());
        assertEquals(reservation.getId(), savedReservation.get().getId());
    }

    @Test
    void listenCalled() {
        // Arrange
        Reservation reservation =
                new Reservation(1L, "John", "Smith", 8, LocalDate.of(2021, 8, 30), LocalDate.of(2021, 9, 5));

        // Act
        producer.send(new ProducerRecord<>(KafkaTopicConfig.CREATE_RESERVATION_TOPIC, 0, UUID.randomUUID().toString(), reservation));
        producer.flush();

        // Assert
        Mockito.verify(kafkaReservationConsumer, Mockito.timeout(5000).times(1))
                .listen(Mockito.any());
    }
}