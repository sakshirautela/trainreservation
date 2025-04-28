package com.bookmytrain.trainreservation.service;

import com.bookmytrain.trainreservation.dto.PaymentRequest;
import com.bookmytrain.trainreservation.dto.PaymentResponse;
import com.bookmytrain.trainreservation.entity.PaymentEntity;
import com.bookmytrain.trainreservation.entity.ReservationEntity;
import com.bookmytrain.trainreservation.repository.PaymentRepository;
import com.bookmytrain.trainreservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public PaymentResponse processPayment(PaymentRequest request) {
        ReservationEntity reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        // In a real app, integrate with payment gateway like Stripe, Razorpay, etc.
        // This is a mock implementation

        PaymentEntity payment = new PaymentEntity();
        payment.setReservation(reservation);
        payment.setAmount(reservation.getTotalFare());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setPaymentTime(LocalDateTime.now());
        payment.setTransactionId(UUID.randomUUID().toString());

        if (Math.random() > 0.1) { // 90% success rate for demo
            payment.setStatus("SUCCESS");
            reservation.setStatus("CONFIRMED");
            paymentRepository.save(payment);
            reservationRepository.save(reservation);
            return new PaymentResponse(true, "Payment successful", payment.getTransactionId());
        } else {
            payment.setStatus("FAILED");
            paymentRepository.save(payment);
            return new PaymentResponse(false, "Payment failed", null);
        }
    }
}