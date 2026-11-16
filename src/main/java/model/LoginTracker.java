package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LoginTracker {

    private static final String FILE_NAME = "Appointment Scheduler\\login_activity.txt";

    /**
     * Logs a login attempt to the login_activity.txt file.
     *
     * @param username  The username used in the login attempt.
     * @param isSuccess True if the login was successful, false otherwise.
     */
    public static void logLoginAttempt(String username, boolean isSuccess) {
        ZonedDateTime timestamp = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String status = isSuccess ? "SUCCESS" : "FAILURE";
        String logEntry = String.format("[%s] [%s] Username: %s - %s%n", timestamp.format(formatter), timestamp.getZone(), username, status);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace(); // This should help if something is wrong
        }
    }
}
