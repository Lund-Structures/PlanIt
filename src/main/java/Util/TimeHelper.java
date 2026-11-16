package Util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;



public class TimeHelper {

    /**
     * convertLocalToEastern. Coverts local time to eastern time.
     * @param timeToConvert LocalDateTime
     * @return LocalDateTime converted to eastern time
     */
    public static LocalDateTime convertLocalToEastern(LocalDateTime timeToConvert) {
        // Eastern Time Zone
        ZoneId easternTime = ZoneId.of("America/New_York");
        ZoneId localZone = ZoneId.systemDefault();
        // convert to eastern time
        ZonedDateTime currentLocalTime = timeToConvert.atZone(localZone);
        ZonedDateTime currentEasternTime = currentLocalTime.withZoneSameInstant(easternTime);
        // Return eastern time
        return currentEasternTime.toLocalDateTime();
    }

    /**
     * compareWithBusinessHours. Compares local time to eastern business hours of 8AM to 10PM.
     * Returns true if hours are between business hours.
     * @param timeToCompare LocalDateTime
     * @return boolean, true or false depending on where the local hour falls between.
     */
    public static boolean compareWithBusinessHours(LocalDateTime timeToCompare) {

        // set starting and end hours
        LocalTime initialStartTime = LocalTime.of(7,59);
        LocalTime initialEndTime = LocalTime.of(22, 0);
        ZoneId easternZoneId = ZoneId.of("America/New_York");
        ZonedDateTime easternStart = ZonedDateTime.of(timeToCompare.toLocalDate(), initialStartTime, easternZoneId);
        ZonedDateTime easternEnd = ZonedDateTime.of(timeToCompare.toLocalDate(), initialEndTime, easternZoneId);

        // EST to compare with
        LocalDateTime easternTimeToCompare = convertLocalToEastern(timeToCompare);

//        System.out.println("Converting time to compare to eastern time");
//        System.out.println("local time: " + timeToCompare);
//        System.out.println("converted eastern time: " + easternTimeToCompare);
//        System.out.println("comparing time is after start hour: " + easternStart + " is " + easternTimeToCompare.isAfter(ChronoLocalDateTime.from(easternStart)));
//        System.out.println("comparing time is before end hour: " + easternEnd + " is " + easternTimeToCompare.isBefore(ChronoLocalDateTime.from(easternEnd)));

        // return true if in between eastern business hours, otherwise false
        return easternTimeToCompare.isAfter(ChronoLocalDateTime.from(easternStart)) && easternTimeToCompare.isBefore(ChronoLocalDateTime.from(easternEnd));
    }

}
