package com.presight.ai.entities;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.ToString;

@Data
public class PersonMetaData {
    private ColorEnum eyeColor;
    private ColorEnum HairColor;
    private double height;

    @Override
    public String toString() {
        return "PersonMetaData{" +
                " eyeColor=" + eyeColor +
                " HairColor=" + HairColor +
                " height=" + height +
                '}';
    }
}
