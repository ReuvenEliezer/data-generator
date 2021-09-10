package com.presight.ai.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.presight.ai.configuration.Config;
import com.presight.ai.entities.*;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CsvGenerator {

    private static final String CALLS_CSV_FILE = "./calls.csv";
    private static final String PEOPLE_CSV_FILE = "./people.csv";

    private static final Random random = new Random();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final char[] charArray = IntStream.rangeClosed('a', 'z')
            .mapToObj(c -> "" + (char) c)
            .collect(Collectors.joining()).toCharArray();

    public CsvGenerator() {
    }

    public static void generate() {
        //https://stackoverflow.com/questions/27952472/serialize-deserialize-java-8-java-time-with-jackson-json-mapper
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.findAndRegisterModules();
        Set<String> phones = generatePersons();
        generateCalls(phones);
    }

    private static void generateCalls(Set<String> phones) {
        Map<Integer, String> indexToPhoneMap = new HashMap<>();
        for (String phone : phones) {
            indexToPhoneMap.put(indexToPhoneMap.size(), phone);
        }

        List<Call> callSet = new ArrayList<>();
        for (int i = 0; i < Config.totalCalls; i++) {
            int srcIndex = random.nextInt(indexToPhoneMap.size());
            String sourcePhone = indexToPhoneMap.get(srcIndex);
            //replace last with src to avoid calling from same source/des
            indexToPhoneMap.replace(srcIndex, indexToPhoneMap.get(indexToPhoneMap.size() - 1));
            indexToPhoneMap.replace(indexToPhoneMap.size() - 1, sourcePhone);

            int callsForEachPhone = getRandom(1, Config.maxCallsForEachPhone + 1);
            for (int j = 0; j < callsForEachPhone && callSet.size() < Config.totalCalls; j++) {
                int desIndex = random.nextInt(indexToPhoneMap.size() - 1);
                String desPhone = indexToPhoneMap.get(desIndex);
                Call call = new Call(sourcePhone, desPhone);
                call.setRegineFrom(randomEnum(RegineTypeEnum.class));
                call.setRegineTo(randomEnum(RegineTypeEnum.class));
                call.setCalTime(getRandom(Config.callTimeSince, LocalDateTime.now(ZoneOffset.UTC)));
                call.setCallDuration(Duration.ofSeconds(getRandom(1, ((int) Config.maxCallDuration.getSeconds()) + 1)));
                callSet.add(call);
            }
        }

        writerCsv(callSet.stream().sorted(Comparator.comparing(Call::getCalTime)), CALLS_CSV_FILE);
    }

    private static <T extends Enum> T randomEnum(Class<T> clazz) {
        int index = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[index];
    }

    private static LocalDateTime getRandom(LocalDateTime from, LocalDateTime to) {
        Duration between = Duration.between(from, to);
        int randomSec = getRandom(1, ((int) between.getSeconds()) + 1);
        return LocalDateTime.from(from).plus(Duration.ofSeconds(randomSec));
    }

    private static Set<String> generatePersons() {
        Map<String, Person> phoneToPeopleMap = new HashMap<>();
        for (int i = 0; i < Config.totalPersons; i++) {
            Person person = new Person(i + 1);

            person.setFirstName(generateName());
            person.setLastName(generateName());

            int totalPhones = getRandom(1, Config.maxPhonesForEachPerson + 1);
            for (int j = 0; j < totalPhones; j++) {
                String phoneNum = generatePhoneNum();
                PhoneOwner phoneOwner = new PhoneOwner(phoneNum);
                person.addPhone(phoneOwner);
                phoneToPeopleMap.put(phoneNum, person);
            }

            person.setGenderTypeEnum(randomEnum(GenderTypeEnum.class));
            person.setCitizen(random.nextBoolean());

            PersonMetaData personMetaData = new PersonMetaData();
            personMetaData.setHeight(getRandom(Config.minHeightInSm, Config.maxHeightInSm + 1));
            personMetaData.setEyeColor(randomEnum(ColorEnum.class));
            personMetaData.setHairColor(randomEnum(ColorEnum.class));
//            try {
//                String s = objectMapper.writeValueAsString(person);
//                Person person1 = objectMapper.readValue(s, Person.class);
//                int d=0;
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
            person.setPersonMetaData(personMetaData);
        }

        writerCsv(phoneToPeopleMap.values().stream().distinct().sorted(Comparator.comparing(Person::getIdentificationNum)), PEOPLE_CSV_FILE);
        return phoneToPeopleMap.keySet();
    }

    private static <T> void writerCsv(Stream<T> stream, String csvFileName) {
        try (Writer writer = Files.newBufferedWriter(Paths.get(csvFileName))) {
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .build();

            beanToCsv.write(stream);
        } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            e.printStackTrace();
        }
    }

    private static int getRandom(int fromInclusive, int toExclusive) {
        return random.nextInt(toExclusive - fromInclusive) + fromInclusive;
    }

    private static String generateName() {
        StringBuilder name = new StringBuilder();
        int nameLength = getRandom(1, Config.maxCharNum + 1);
        for (int i = 0; i < nameLength; i++) {
            name.append(charArray[random.nextInt(charArray.length - 1)]);
        }
        return name.toString();
    }

    private static String generatePhoneNum() {
        StringBuilder phoneNum = new StringBuilder().append("+");
        for (int i = 0; i < Config.phoneDigits; i++) {
            phoneNum.append(random.nextInt(10));
        }
        return phoneNum.toString();
    }
}
