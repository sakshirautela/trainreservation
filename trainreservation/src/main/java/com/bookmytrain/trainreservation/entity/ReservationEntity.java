package com.bookmytrain.trainreservation.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "train_id")
    private TrainEntity train;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private LocalDateTime reservationTime;
    private String status; // CONFIRMED, CANCELLED, etc.
    private int numberOfSeats;
    private double totalFare;
    private String pnrNumber;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private com.train.reservation.entity.PaymentEntity payment;

    @PrePersist
    public void generatePnr() {
        this.pnrNumber = "PNR" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.reservationTime = LocalDateTime.now();
        this.status = "PENDING_PAYMENT";
    }
}