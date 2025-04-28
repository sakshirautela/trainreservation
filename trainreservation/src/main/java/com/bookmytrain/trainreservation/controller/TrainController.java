package com.bookmytrain.trainreservation.controller;

import com.bookmytrain.trainreservation.service.TrainService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TrainController.java
@RestController
@RequestMapping("/api/trains")
@RequiredArgsConstructor
public class TrainController {
    private final TrainService trainService;

    @GetMapping("/search")
    public ResponseEntity<List<Train>> searchTrains(
            @RequestParam String source,
            @RequestParam String destination) {
        return ResponseEntity.ok(trainService.searchTrains(source, destination));
    }

    @PostMapping
    public ResponseEntity<Train> addTrain(@RequestBody Train train) {
        return ResponseEntity.ok(trainService.saveTrain(train));
    }
}

