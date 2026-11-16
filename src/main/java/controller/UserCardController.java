package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import model.User;

/**
 * Controller for the user card UI component.
 * This class manages the display of user details such as name, user ID, and password.
 */
public class UserCardController {

    //----------------------------------------------FXML Elements----------------------------------------------//
    @FXML
    private Button PasswordButton;

    @FXML
    private Label NameLabel;

    @FXML
    private Button IDButton;

    private User currentUser;


    //------------------------------------------Show Data Methods----------------------------------------------//
    /**
     * Displays the user's ID on the user card when the ID button is clicked.
     *
     * @param event The event triggered by clicking the ID button.
     */
    @FXML
    void showUserID(ActionEvent event) {
        IDButton.setText(String.valueOf(currentUser.getUserID()));
    }

    /**
     * Displays the user's password on the user card when the password button is clicked.
     *
     * @param event The event triggered by clicking the password button.
     */
    @FXML
    void showPassword(ActionEvent event) {
        PasswordButton.setText(currentUser.getPassword());
    }


    //--------------------------------------------Set Methods--------------------------------------------------//
    /**
     * Sets the current user and updates the name label to display the user's name.
     *
     * @param user The user to be displayed on the user card.
     */
    void setUser(User user) {
        this.currentUser = user;
        NameLabel.setText(currentUser.getUserName());
    }
}
