package com.train.reservation.repository;

import com.train.reservation.entity.ReservationEntity;
import com.train.reservation.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    Optional<ReservationEntity> findByPnrNumber(String pnrNumber);
    List<ReservationEntity> findByUser(UserEntity user);
    List<ReservationEntity> findByUserOrderByReservationTimeDesc(UserEntity user);
    boolean existsByTrainIdAndStatus(Long trainId, String status);
}