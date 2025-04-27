package com.bookmytrain.trainreservation.entity;

import jakarta.persistence.Id;

public class RailwayEntity {
    @Id
    private  Object id;
    private String station_name;
    private int trains;
    private String address;
    private String[] whereCanGo;
}
