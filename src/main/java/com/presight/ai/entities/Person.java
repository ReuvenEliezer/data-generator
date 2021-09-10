package com.presight.ai.entities;

import com.opencsv.bean.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@ToString
@NoArgsConstructor //for serializer
public class Person {

    /**
     *:
     * - first name
     * - last name
     * - identical id
     * - phone num
     * - gender type
     * - additional data - color eye
     * - city tion אזרח? y/n
     */


    @Setter(AccessLevel.NONE)
    @CsvBindByName(column = "Id")
    private Integer identificationNum;

    @CsvBindByName(column = "First Name")
    private String firstName;

    @CsvBindByName(column = "Last Name")
    private String lastName;

    @Setter(AccessLevel.NONE)
    @CsvBindAndSplitByName(column = "Phone Owner List", elementType = PhoneOwner.class, writeDelimiter = ",")
    //https://stackoverflow.com/questions/62841670/opencsv-how-to-write-into-csv-with-custom-processing
    private Set<PhoneOwner> phoneNumList;

    @CsvBindByName(column = "Gender")
    private GenderTypeEnum genderTypeEnum;

    @CsvBindByName(column = "Is Citizen")
    private boolean isCitizen;

    @CsvBindByName(column = "Meta Data")
    private PersonMetaData personMetaData;


    public Person(Integer identificationNum) {
        this.identificationNum = identificationNum;
        this.phoneNumList = new HashSet<>();
    }

    public void addPhone(PhoneOwner phoneOwner) {
        phoneNumList.add(phoneOwner);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(identificationNum, person.identificationNum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identificationNum);
    }
}
