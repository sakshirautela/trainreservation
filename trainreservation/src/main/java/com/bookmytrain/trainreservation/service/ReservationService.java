package com.train.reservation.service;

import com.train.reservation.entity.ReservationEntity;
import com.train.reservation.entity.TrainEntity;
import com.train.reservation.entity.UserEntity;
import com.train.reservation.exception.ResourceNotFoundException;
import com.train.reservation.repository.ReservationRepository;
import com.train.reservation.repository.TrainRepository;
import com.train.reservation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TrainRepository trainRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationEntity createReservation(Long trainId, int numberOfSeats) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        TrainEntity train = trainRepository.findById(trainId)
                .orElseThrow(() -> new ResourceNotFoundException("Train not found"));

        if (train.getAvailableSeats() < numberOfSeats) {
            throw new RuntimeException("Not enough seats available");
        }

        ReservationEntity reservation = new ReservationEntity();
        reservation.setTrain(train);
        reservation.setUser(user);
        reservation.setNumberOfSeats(numberOfSeats);
        reservation.setTotalFare(train.getFare() * numberOfSeats);

        // Reserve seats (but don't confirm until payment)
        train.setAvailableSeats(train.getAvailableSeats() - numberOfSeats);
        trainRepository.save(train);

        return reservationRepository.save(reservation);
    }

    // Other methods remain similar but updated with security checks
}