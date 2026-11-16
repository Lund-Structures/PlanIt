package Util;

import javafx.scene.control.Alert;
import model.CustomAlerts;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Utility class for handling database connections using JDBC.
 * This class provides methods to open and close a connection to a MySQL database.
 * It is designed to be used in other parts of the application that need database connectivity.
 *
 * Note: The class is abstract to prevent instantiation, as it is intended to be a utility class.
 */
public abstract class JDBC {

    // Database connection parameters
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName + "?connectionTimeZone=SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // MySQL Driver
    private static final String userName = "sqlUser"; // Username for DB
    private static String password = "Passw0rd!"; // Password for DB
    public static Connection connection;  // Connection Interface for DB

    /**
     * Opens a connection to the database. The method loads the JDBC driver, establishes a connection,
     * and stores it in the static `connection` field.
     *
     * @return The established database connection if successful, null otherwise.
     */
    public static Connection openConnection() {
        try {
            // MySQL JDBC driver
            Class.forName(driver);
            // Establish connection to the database
            connection = DriverManager.getConnection(jdbcUrl, userName, password);
        } catch (Exception e) {
            System.out.println("Could not connect to the database\n" + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the current database connection.
     * If an exception occurs during closing the connection, the stack trace is printed.
     */
    public static void closeConnection() {
        try {

            connection.close();
        } catch (Exception e) {
            System.out.println("Error:" + e.getMessage());
        }
    }
}