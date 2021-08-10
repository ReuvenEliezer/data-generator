package com.presight.ai.entities;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Data
@ToString
public class Call {
    /**
     * phone
     * time
     * duration
     * from
     * to - list
     * start
     * end
     * is answered ?
     * region
     */
    @CsvBindByName(column = "Source")
    private String fromPhoneNum;
    @CsvBindByName(column = "Destination")
    private String toPhoneNum;
    @CsvBindByName(column = "Call Time")
    @CsvDate(value = "yyyy-MM-dd'T'hh:mm:ss.SSS")
    private LocalDateTime calTime;
    @CsvBindByName(column = "Call Duration")
    private Duration callDuration;
    @CsvBindByName(column = "Regine From")
    private RegineTypeEnum regineFrom;
    @CsvBindByName(column = "Regine To")
    private RegineTypeEnum regineTo;

    public Call(String fromPhoneNum, String toPhoneNum) {
        this.fromPhoneNum = fromPhoneNum;
        this.toPhoneNum = toPhoneNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Call call = (Call) o;
        return Objects.equals(fromPhoneNum, call.fromPhoneNum) && Objects.equals(toPhoneNum, call.toPhoneNum) && Objects.equals(calTime, call.calTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromPhoneNum, toPhoneNum, calTime);
    }
}
