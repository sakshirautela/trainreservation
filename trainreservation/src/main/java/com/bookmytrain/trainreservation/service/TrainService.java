package com.bookmytrain.trainreservation.service;

import com.bookmytrain.trainreservation.entity.TrainEntity;
import com.bookmytrain.trainreservation.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainService {

    private final TrainRepository trainRepository;

    /**
     * Get all available trains
     */
    public List<TrainEntity> getAllTrains() {
        return trainRepository.findAll();
    }

    /**
     * Search trains between stations with optional date filter
     */
    public List<TrainEntity> searchTrains(String source, String destination, LocalDate date) {
        if (date != null) {
            // In a real application, you would filter by date using a schedule table
            return trainRepository.findBySourceStationAndDestinationStation(source, destination);
        }
        return trainRepository.findBySourceStationAndDestinationStation(source, destination);
    }

    /**
     * Get train by ID
     */
    public TrainEntity getTrainById(Long id) {
        return trainRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Train", "id", id));
    }

    /**
     * Add a new train
     */
    public TrainEntity addTrain(TrainEntity train) {
        // Validate train number is unique
        if (trainRepository.existsByTrainNumber(train.getTrainNumber())) {
            throw new IllegalArgumentException("Train with number " + train.getTrainNumber() + " already exists");
        }

        // Set available seats equal to total seats for new trains
        train.setAvailableSeats(train.getTotalSeats());
        return trainRepository.save(train);
    }

    /**
     * Update train information
     */
    public TrainEntity updateTrain(Long id, TrainEntity trainDetails) {
        TrainEntity train = getTrainById(id);

        // Update only mutable fields
        train.setName(trainDetails.getName());
        train.setSourceStation(trainDetails.getSourceStation());
        train.setDestinationStation(trainDetails.getDestinationStation());
        train.setDepartureTime(trainDetails.getDepartureTime());
        train.setArrivalTime(trainDetails.getArrivalTime());
        train.setFare(trainDetails.getFare());

        // When total seats change, adjust available seats accordingly
        int seatDifference = trainDetails.getTotalSeats() - train.getTotalSeats();
        train.setTotalSeats(trainDetails.getTotalSeats());
        train.setAvailableSeats(train.getAvailableSeats() + seatDifference);

        return trainRepository.save(train);
    }

    /**
     * Delete a train
     */
    public void deleteTrain(Long id) {
        TrainEntity train = getTrainById(id);

        // Check if there are any active reservations
        if (train.getAvailableSeats() != train.getTotalSeats()) {
            throw new IllegalStateException("Cannot delete train with active reservations");
        }

        trainRepository.delete(train);
    }

    /**
     * Get available seat count for a train
     */
    public int getAvailableSeats(Long trainId) {
        return getTrainById(trainId).getAvailableSeats();
    }

    /**
     * Update seat availability (used when making/canceling reservations)
     */
    protected void updateSeatAvailability(Long trainId, int seatChange) {
        TrainEntity train = getTrainById(trainId);
        int newAvailableSeats = train.getAvailableSeats() + seatChange;

        if (newAvailableSeats < 0 || newAvailableSeats > train.getTotalSeats()) {
            throw new IllegalArgumentException("Invalid seat adjustment");
        }

        train.setAvailableSeats(newAvailableSeats);
        trainRepository.save(train);
    }
}