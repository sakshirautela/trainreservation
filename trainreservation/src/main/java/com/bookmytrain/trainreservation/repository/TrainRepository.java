package com.train.reservation.repository;

import com.train.reservation.entity.TrainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainRepository extends JpaRepository<TrainEntity, Long> {
    List<TrainEntity> findBySourceStationAndDestinationStation(String source, String destination);
    boolean existsByTrainNumber(String trainNumber);
    List<TrainEntity> findByOrderByFareAsc();
    List<TrainEntity> findByOrderByDepartureTimeAsc();
}