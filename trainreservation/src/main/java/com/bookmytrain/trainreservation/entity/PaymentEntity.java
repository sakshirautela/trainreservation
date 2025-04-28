package com.bookmytrain.trainreservation.entity;

import com.bookmytrain.trainreservation.entity.ReservationEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;

    private String paymentId;
    private String paymentMethod;
    private double amount;
    private LocalDateTime paymentTime;
    private String status; // SUCCESS, FAILED, PENDING
    private String transactionId;
}