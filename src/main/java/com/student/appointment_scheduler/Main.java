package com.student.appointment_scheduler;

import Util.JDBC;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The main entry point for the Appointment Scheduler application.
 * This class initializes the JavaFX application and manages the database connection.
 */
public class Main extends Application {

    /**
     * The main database connection, initialized when the application starts.
     */
    public static Connection conn = JDBC.openConnection();

    /**
     * Resource bundle for localized messages, based on the system's default locale.
     */
    public static final ResourceBundle messages = ResourceBundle.getBundle("Messages", Locale.getDefault());

    /**
     * Starts the JavaFX application by loading the login scene.
     *
     * @param stage The primary stage for the application.
     * @throws IOException if the login scene FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/LoginScene.fxml"));
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png"))); // Sets window icon
        Parent root = loader.load();

        LoginController controller = loader.getController();
        controller.setConnection(conn); // Passes database connection to the controller

        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     * The main entry point of the application.
     * This method launches the JavaFX application and ensures the database connection is closed upon exit.
     *
     * @param args Command-line arguments (not used).
     * @throws SQLException if a database error occurs.
     * @throws ClassNotFoundException if the JDBC driver is not found.
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        launch(args); // Starts the JavaFX application

        JDBC.closeConnection(); // Closes database connection on application exit
    }
}
