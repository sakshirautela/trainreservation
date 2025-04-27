package com.bookmytrain.trainreservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    private Object id;
    private String firstname;
    private String lastname;
    private String passWord;
    private String address;
    private String mailId;
    private long phoneNo;

}
