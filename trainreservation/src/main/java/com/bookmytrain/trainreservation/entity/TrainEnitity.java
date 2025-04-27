package com.bookmytrain.trainreservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class TrainEnitity {
    @Id
    private Object train_no;
    private String train_name;
    private String from_station;
    private String to_station;
    private Integer seats;
    private Double fare;

}
