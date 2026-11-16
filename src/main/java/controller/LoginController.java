package controller;

import Util.DAO.UserDAOImp;
import Util.DataQuery;
import com.student.appointment_scheduler.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.CustomAlerts;
import model.LoginTracker;
import model.User;

/**
 * Controller class for handling the login functionality of the application.
 * It manages user authentication, localization settings, and navigates to the main menu upon successful login.
 */
public class LoginController implements Initializable {


    //----------------------------------------------FXML Elements----------------------------------------------//
    @FXML
    private Button LoginButton;

    @FXML
    private Label RegionMessage;

    @FXML
    private Label WelcomeMessage;

    @FXML
    private PasswordField PasswordTextField;

    @FXML
    private Text LoginErrorMessage;

    @FXML
    private TextField UsernameTextField;


    //--------------------------------------------Instant Variables--------------------------------------------//
    private String userName;
    private Connection conn;
    private User currentUser;
    private String password;


    //--------------------------------------Login Controller Initialization-------------------------------------//
    /**
     * Initializes the controller class. Handles localization and button hover effects.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            //Checks and sets FXML elements to the regions language
            if(Locale.getDefault().getLanguage().equals("fr")) {
                LoginButton.setText(Main.messages.getString("login_button"));
                UsernameTextField.setPromptText(Main.messages.getString("username"));
                PasswordTextField.setPromptText(Main.messages.getString("password"));
                LoginErrorMessage.setText(Main.messages.getString("login_error_message"));
                WelcomeMessage.setText(Main.messages.getString("welcome"));
                RegionMessage.setText(Main.messages.getString("region") + " " + Locale.getDefault().getCountry());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /**
         * Adds a hover effect to the {@code LoginButton} by changing its background and text color
         * when the mouse pointer enters the button area.
         * <p>
         * This lambda expression sets an event handler for the {@link javafx.scene.input.MouseEvent#MOUSE_ENTERED}
         * event. When triggered, it applies a new inline CSS style to the button to visually indicate
         * that it is being hovered over. The background color changes to a bright cyan, and the text color
         * changes to white for better contrast and user feedback.
         * <p>
         * The lambda expression {@code e -> ...} implements the {@link javafx.event.EventHandler} functional interface.
         */
        LoginButton.setOnMouseEntered(e -> LoginButton.setStyle("-fx-background-color: #5eddff; -fx-text-fill: white;"));

        /**
         * Resets the {@code LoginButton} style when the mouse exits the button area.
         * <p>
         * This lambda expression listens for the {@link javafx.scene.input.MouseEvent#MOUSE_EXITED} event and
         * restores the button’s original styling. It ensures a smooth and intuitive user interface by
         * removing the hover effect applied during LoginButton.setOnMouseEntered.
         * <p>
         * The expression {@code e -> ...} is a lambda that implements {@link javafx.event.EventHandler},
         * allowing concise event handling for UI interactions.
         */
        LoginButton.setOnMouseExited(e -> LoginButton.setStyle("-fx-background-color: #00a8e8; -fx-text-fill: white;"));
    }



    //--------------------------------------Login Verification and Logic---------------------------------------//
    /**
     * Attempts to authenticate the user based on the provided username and password.
     * If successful, it navigates to the main menu; otherwise, an error message is displayed.
     *
     * @throws SQLException If a database error occurs during authentication.
     */
    @FXML
    private void attemptLogin () {
        String sql = "SELECT Password FROM users WHERE User_Name = ?";

        boolean loginToken = false;
        userName = UsernameTextField.getText();
        password = PasswordTextField.getText();

        try {
            UserDAOImp userDAO = new UserDAOImp(Main.conn);

            //Sets the current user, can be null. Throws error for bad communication with DataBase
            currentUser = userDAO.setCurrentUser(userName, password);
            if (currentUser == null) {
                LoginErrorMessage.setVisible(true);
            } else {
                //Check if there is an upcoming appointment for the user
                checkUpcomingAppointment();
                //set login token to true
                loginToken = true;
            }

        } catch (SQLException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("SQL Error", Main.messages.getString("server_Error"));
        }

        if (loginToken) {
            // Log successful login to the login tracker
            LoginTracker.logLoginAttempt(userName, loginToken);

            try{
                //Load main menu scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/MainMenu.fxml"));
                Parent root = loader.load();

                //set data needed for main menu scene
                MainMenuController mainMenuController = loader.getController();
                mainMenuController.setUser(currentUser);
                mainMenuController.setConn(conn);

                Stage mainMenuStage = new Stage();
                mainMenuStage.setScene(new Scene(root));
                mainMenuStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/Logo.png"))));
                mainMenuStage.show();

                // Close login scene
                Stage loginStage = (Stage) UsernameTextField.getScene().getWindow();
                loginStage.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        } else {
            LoginErrorMessage.setVisible(true);
            //Log Unsuccessful Login
            LoginTracker.logLoginAttempt(userName, loginToken);
        }

    }

    /**
     * Checks if there are any upcoming appointments for the logged-in user.
     * If an appointment is within the next 15 minutes, an alert is displayed.
     *
     * @throws SQLException If a database error occurs while checking appointments.
     */
    private void checkUpcomingAppointment() throws SQLException {
        int id;
        LocalDateTime appointment;


        //Query Database for users appointments and save them to a result set
        DataQuery.setPreparedStatement(Main.conn, "SELECT * FROM appointments where User_ID = ?");
        PreparedStatement ps = DataQuery.getPreparedStatement();
        ps.setInt(1, currentUser.getUserID());
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            appointment = rs.getTimestamp("Start").toLocalDateTime();
            id = rs.getInt("Appointment_ID");
            //if true there is an appointment for the user within 15min
            if(!appointment.isBefore(LocalDateTime.now()) && !appointment.isAfter(LocalDateTime.now().plusMinutes(15))) {
                CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
                customAlert.genericAlert("Appointment alert", Main.messages.getString("appointment_on_login") +
                                                                    "\n\nAppointment ID: " + id +
                                                                    "\nDate/Time: " + appointment);
                return;
            }
        }
        CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
        customAlert.genericAlert("No appointment",Main.messages.getString("no_appointment_on_login"));
    }


    //--------------------------------------------Helper Methods-----------------------------------------------//
    /**
     * Sets the database connection for the login process.
     *
     * @param mainConn The database connection to be used.
     */
    public void setConnection(Connection mainConn) {
        this.conn = mainConn;
    }

    /**
     * Hides the login error message when a key is pressed.
     *
     * @param keyEvent The KeyEvent triggered by user input.
     */
    @FXML
    private void hideError(KeyEvent keyEvent) {
        LoginErrorMessage.setVisible(false);
    }

}