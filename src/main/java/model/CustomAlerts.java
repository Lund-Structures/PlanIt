package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * A custom alert class that extends {@link Alert} to create and manage custom alert dialogs.
 * This class provides methods for showing basic alerts and confirmation dialogs with custom messages and icons.
 */
public class CustomAlerts extends Alert {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private AlertType alertType;
    private String title;
    private String message;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a CustomAlerts object with a specified alert type.
     * @param alertType the type of alert (e.g., ERROR, WARNING, INFORMATION, etc.)
     */
    public CustomAlerts(AlertType alertType) {
        super(alertType);
    }


    //-------------------------------------------------Methods-------------------------------------------------//
    /**
     * Displays a generic alert with an error icon, custom title, and message.
     * @param title the title of the alert window
     * @param message the content message of the alert
     */
    public void genericAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Set the window icon
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        alert.showAndWait();
    }

    /**
     * Displays a confirmation alert with "Yes" and "No" buttons.
     * Returns true if the user clicks "Yes".
     *
     * @param title the title of the alert window
     * @param message the content message of the alert
     * @return true if the user clicks "Yes", false if "No" is clicked
     */
    public Boolean returnTrueFalse(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Are you sure?");
        alert.setContentText(message);

        // Set the window icon
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));

        // Customize buttons
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        // Set the button types
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and capture the user's choice
        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == yesButton;
    }
}