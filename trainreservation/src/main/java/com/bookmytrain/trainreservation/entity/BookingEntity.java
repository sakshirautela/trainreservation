package com.bookmytrain.trainreservation.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BookingEntity {
    private String mailId;
    private String train_no;
    private String date;
    private String from_station;
    private String to_station;
    private int seats;
    private String[] seat;
    private Double amount;
}
