package com.presight.ai.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor //for serializer
public class PhoneOwner {

    @CsvBindByName(column = "Phone Number")
    private String phoneNum;
    @CsvDate(value = "yyyy-MM-dd'T'hh:mm:ss.SSS")
    @CsvBindByName(column = "Ownership Start Date Time")
    private LocalDateTime ownershipStartDateTime;
    @CsvDate(value = "yyyy-MM-dd'T'hh:mm:ss.SSS")
    @CsvBindByName(column = "Ownership End Date Time")
    private LocalDateTime ownershipEndDateTime;

    public PhoneOwner(String phoneNum) {
        this.phoneNum = phoneNum;
        this.ownershipStartDateTime = LocalDateTime.MIN;
    }
}
