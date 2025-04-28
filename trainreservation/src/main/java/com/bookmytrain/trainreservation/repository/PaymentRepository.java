package com.bookmytrain.trainreservation.repository;

import com.bookmytrain.trainreservation.entity.PaymentEntity;
import com.bookmytrain.trainreservation.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByReservation(ReservationEntity reservation);
    List<PaymentEntity> findByStatus(String status);
    Optional<PaymentEntity> findByTransactionId(String transactionId);
}