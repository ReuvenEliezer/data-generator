package com.presight.ai.configuration;

import javax.xml.crypto.Data;
import java.time.*;
import java.util.Date;

public class Config {

    //person config
    public static int totalPersons = 20;
    public static int maxCharNum = 10;
    public static int maxPhonesForEachPerson = 3;
    public static int phoneDigits = 12;
    public static int maxHeightInSm = 240;
    public static int minHeightInSm = 10;

    //calls config
    public static int totalCalls = 100;
    public static int maxCallsForEachPhone = 10;
    public static Duration maxCallDuration = Duration.ofHours(2);
    public static LocalDateTime callTimeSince = LocalDateTime.of(2019, 1, 1, 00, 00).atZone(ZoneOffset.UTC).toLocalDateTime();

}
