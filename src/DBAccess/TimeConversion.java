package DBAccess;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/** The TimeConversion class. This class contains all of the methods that I created to handle converting time variables
 * that are involved in my project. */
public class TimeConversion {

    // This will convert localdate time to database timestamp
    /** The localToTimeStamp() method. This method accepts a LocalDateTime variable type and converts it into a TimeStamp
     * by calling the Timestamp.valueof method on the LocalDateTime parameter that was provided.
     * @param localDateTime This is the LocalDateTime variable that is passed for converting
     * @return Timestamp.valueOf(localDateTime)*/
    public static Timestamp localToTimeStamp(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }

    // This method will convert the database timestamp into local time
    /** The timeStampToLocal() method. This method will accept a Timestamp variable as a parameter and assigns it to dbTime
     * will create a LocalDateTime variable which will be used to store the result of converting the dbTime parameter into
     * a LocalDateTime type.
     * @param dbTime The dbTime parameter is a variable that is of the type Timestamp.
     * @return localTime The localTime is of the type LocalDateTime and contains the converted Timestamp parameter.*/
    public static LocalDateTime timeStampToLocal(Timestamp dbTime) {
        LocalDateTime localTime;
        localTime = dbTime.toLocalDateTime();

        return localTime;
    }

    /** The localTimeDatetoDate() method. This method takes a LocalDateTime variable as a parameter and then converts it
     * into a LocalDate type.
     * @param localDateTime This parameter is the passed LocalDateTime variable.
     * @return localDate This variable is of the LocalDate type and contains the converted LocalDateTime.*/
    public static LocalDate localTimeDateToDate(LocalDateTime localDateTime) {
        LocalDate localDate;
        localDate = localDateTime.toLocalDate();

        return localDate;
    }

    /** The localTimeDateToTime() method. This method will take a LocalDateTime variable as a parameter and converts it
     *  into the local time by calling the toLocalTime() method.
     *  @param localDateTime The LocalDateTime variable that is passed to the method.
     *  @return localTime This is the LocalTime variable that is returned and contains the converted LocalDateTime's LocalTime.*/
    public static LocalTime localTimeDateToTime(LocalDateTime localDateTime) {
        LocalTime localTime;
        localTime = localDateTime.toLocalTime();

        return localTime;
    }

    /** The localDateLocalTimeToLDT() method. This method will accept two parameters, the first is a LocalDate object
     * and the other is a LocalTime object. These two objects are used to create a LocalDateTime object which is returned.
     * @param date This is the LocalDate object that is passed and used to create a LocalDateTime object.
     * @param time The LocalTime object that is passed into the method.
     * @return localDateTime Is the created LocalDateTime object.*/
    public static LocalDateTime localDateLocalTimeToLDT(LocalDate date, LocalTime time) {
        LocalDateTime localDateTime;
        localDateTime = LocalDateTime.of(date, time);

        return localDateTime;
    }

    /** The stringToLocalTime() method. This method will take a String parameter and parses that string to create a LocalTime
     * object. This is done by using the DateTimeFormatter class which will parse the string and try to create a LocalTime
     * object that matches the given pattern.
     * @param timeString The given string variable.
     * @return The LocalTime object that was created.
     * */
    public static LocalTime stringToLocalTime(String timeString){
        LocalTime time;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        time = LocalTime.parse(timeString, timeFormatter);

        return time;
    }

    /** The stringToLocalDate() method. This method will take a String parameter and parses that string to create a LocalDate
     * object. This is done by using the DateTimeFormatter class which will parse the string and try to create a LocalDate
     * object that matches the given pattern.
     * @param dateString The given string variable.
     * @return The returned LocalDate object that was created.*/
    public static LocalDate stringToLocalDate(String dateString){
        LocalDate date;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        date = LocalDate.parse(dateString, dateFormatter);

        return date;
    }
}
