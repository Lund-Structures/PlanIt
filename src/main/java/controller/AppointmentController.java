package controller;

import Util.DAO.*;
import Util.DataQuery;
import Util.TimeHelper;
import com.student.appointment_scheduler.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.*;
import Util.DAO.CustomersDAOImp;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class AppointmentController implements Initializable {

    //----------------------------------------------FXML Elements----------------------------------------------//
    // Buttons
    @FXML
    private Button AddSubmitButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Button ModSubmitButton;

    // Text Fields
    @FXML
    private TextField AppointmentField;

    @FXML
    private TextField EndTimeHR;

    @FXML
    private TextField EndTimeMin;

    @FXML
    private TextField LocationField;

    @FXML
    private TextField StartTimeHR;

    @FXML
    private TextField StartTimeMin;

    @FXML
    private TextField TitleField;

    @FXML
    private TextField TypeField;

    @FXML
    private TextArea DescriptionField;

    // Date Pickers
    @FXML
    private DatePicker EndDateField;

    @FXML
    private DatePicker StartDateField;

    // Choice/Combo Boxes
    @FXML
    private ChoiceBox<String> EndAmPm;

    @FXML
    private ChoiceBox<String> StartAmPm;

    @FXML
    private ComboBox<String> ContactField;

    @FXML
    private ComboBox<String> CustomerField;

    @FXML
    private ComboBox<String> UserField;

    // Labels
    @FXML
    private Label AddAppLabel;

    @FXML
    private Label ModAppLabel;

    @FXML
    private ImageView AddAppImg;

    @FXML
    private ImageView ModAppImg;


    //--------------------------------------------Instant Variables--------------------------------------------//
    // DAO objects
    AppointmentDAOImp appointmentDAO = new AppointmentDAOImp(Main.conn);
    ContactDAOImp contactDAO = new ContactDAOImp(Main.conn);
    CustomersDAOImp customerDAO = new CustomersDAOImp(Main.conn);
    UserDAOImp userDAO = new UserDAOImp(Main.conn);
    // Lists
    ObservableList<Contact> contactList = FXCollections.observableArrayList();
    ObservableList<Customer> customerList = FXCollections.observableArrayList();
    ObservableList<User> userList = FXCollections.observableArrayList();
    // AppointmentController scope variables
    Appointment selectedAppointment = null;
    Boolean addView = true;
    User user = null;


    //--------------------------------Appointment Controller Initialization-------------------------------------//
    /**
     * Initializes the appointment controller and prefills existing customer and user information
     *
     * @param url The location used to resolve relative paths for the root object
     * @param resourceBundle The resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFields();
    }

    /**
     * Initializes dropdown fields in the form with necessary data.
     */
    @FXML
    void initializeFields () {
        try {
            customerList = customerDAO.getAllCustomers();
            contactList = contactDAO.getAllContacts();
            userList = userDAO.getAllUsers();
        } catch (SQLException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Database Error", "Couldn't auto populate combo boxes from the database");
        }
        for (Customer customer : customerList) {

            CustomerField.getItems().add(String.valueOf(customer.getCustomerID()) + ")  " + customer.getCustomerName());
        }
        for (Contact contact : contactList) {
            ContactField.getItems().add(String.valueOf(contact.getContactID()) + ")  " + contact.getName());
        }
        for (User user : userList) {
            UserField.getItems().add(user.getUserID() + ") " + user.getUserName());  //Change other for loops to look like this one.
        }
        //Populate information in the Start time fields
        StartAmPm.getItems().addAll("Am", "Pm");
        StartAmPm.setValue("Am");
        //Populate information in the End time fields
        EndAmPm.getItems().addAll("Am", "Pm");
        EndAmPm.setValue("Am");
        //Set DatePicker to reject manual date input
        StartDateField.getEditor().setDisable(true);
        EndDateField.getEditor().setDisable(true);
    }

    /**
     * Receives and sets the current logged-in user data.
     *
     * @param userData The user object representing the currently logged-in user.
     */
    public void userData(User userData) {
        this.user = userData;
        if (addView) {
            UserField.setValue(String.valueOf(user.getUserID()));
        } else if (!addView) {
            UserField.setValue(String.valueOf(user.getUserID()));
        }
    }


    //--------------------------------------CRUD Event Handlers-------------------------------------------------///
    /**
     * Loads and prepares the UI for modifying an existing appointment.
     *
     * @param event The action event (Clicking on the Modify submit Appointment button).
     */
    @FXML
    void modifyAppointment(ActionEvent event) {
        System.out.println("You Clicked The Modify Appointment Button"); //MANAGE CODE!!!!!!!!!!!!!

        try {
            validateRequiredFields();
            int appointmentID = validateInteger(AppointmentField.getText(), "There seems to be a conflict with the appointment ID");
            int userID = validateInteger(UserField.getValue(), "Invalid User ID. Please enter a valid numeric ID.");
            int customerID = validateInteger(CustomerField.getValue(), "Invalid Customer ID. Please enter a valid numeric ID.");
            int contactID = validateInteger(ContactField.getValue(), "Invalid Contact ID. Please enter a valid numeric ID.");

            Timestamp startDateTime = getTimestamp(StartDateField.getValue(), StartTimeHR.getText(), StartTimeMin.getText(), StartAmPm.getValue(), "Start");
            Timestamp endDateTime = getTimestamp(EndDateField.getValue(), EndTimeHR.getText(), EndTimeMin.getText(), EndAmPm.getValue(), "End");

            checkOverlap(startDateTime, endDateTime, customerID, appointmentID);

            if (endDateTime.before(Timestamp.valueOf(LocalDate.now().atStartOfDay()))) {
                throw new RuntimeException("The end date cannot be before today's date.");
            }

            appointmentDAO.updateAppointment(
                    appointmentID,
                    TitleField.getText().trim(),
                    DescriptionField.getText().trim(),
                    LocationField.getText().trim(),
                    TypeField.getText().trim(),
                    startDateTime,
                    endDateTime,
                    user.getUserName(),
                    customerID,
                    userID,
                    contactID
            );

            // Close add appointment scene
            Stage addAppointment = (Stage) AddSubmitButton.getScene().getWindow();
            addAppointment.close();

        } catch (SQLIntegrityConstraintViolationException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("ID Not Found", "The ID could not be matched in the database" + e.getMessage());
        } catch (SQLException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("SQL Error", "There was a problem communicating with the database " + e.getMessage());
        } catch (IllegalArgumentException integerException) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("Invalid Numeric Input", integerException.getMessage());
        } catch (RuntimeException fieldException) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("Field Error", fieldException.getMessage());
        }
    }

    /**
     * Sets the appointment fields in the form when modifying an appointment.
     */
    @FXML
    void setAppointment (Appointment appointment) {

        this.selectedAppointment = appointment;

        AppointmentField.setText(String.valueOf(selectedAppointment.getAppointmentID()));
        CustomerField.setValue(String.valueOf(selectedAppointment.getCustomerID()));
        ContactField.setValue(String.valueOf(selectedAppointment.getContactID()));
        DescriptionField.setText(selectedAppointment.getDescription());
        LocationField.setText(selectedAppointment.getLocation());
        TitleField.setText(selectedAppointment.getTitle());
        TypeField.setText(selectedAppointment.getMeetingType());
        StartDateField.setValue(selectedAppointment.getStartTime().toLocalDate());
        EndDateField.setValue(selectedAppointment.getEndTime().toLocalDate());
        StartTimeHR.setText(formatTimeFields(selectedAppointment.getStartTime(), "start"));
        StartTimeMin.setText(String.valueOf(selectedAppointment.getStartTime().getMinute()));
        EndTimeHR.setText(formatTimeFields(selectedAppointment.getEndTime(), "end"));
        EndTimeMin.setText(String.valueOf(selectedAppointment.getEndTime().getMinute()));


        int startTime = selectedAppointment.getStartTime().getHour();
        int endTime = selectedAppointment.getEndTime().getHour();
        if(startTime > 11) {
            StartAmPm.setValue("Pm");
        }else {
            StartAmPm.setValue("Am");
        }
        if (endTime > 11) {
            EndAmPm.setValue("Pm");
        } else {
            StartAmPm.setValue("Am");
        }

    }

    /**
     * Handles the logic for submitting a new appointment.
     *
     * @param event The action event (Clicking on the submit button).
     */
    @FXML
    void submitAppointment(ActionEvent event) {
        try {
            validateRequiredFields();
            int userID = validateInteger(UserField.getValue(), "Invalid User ID. Please enter a valid numeric ID.");
            int customerID = validateInteger(CustomerField.getValue(), "Invalid Customer ID. Please enter a valid numeric ID.");
            int contactID = validateInteger(ContactField.getValue(), "Invalid Contact ID. Please enter a valid numeric ID.");

            Timestamp startDateTime = getTimestamp(StartDateField.getValue(), StartTimeHR.getText(), StartTimeMin.getText(), StartAmPm.getValue(), "Start");
            Timestamp endDateTime = getTimestamp(EndDateField.getValue(), EndTimeHR.getText(), EndTimeMin.getText(), EndAmPm.getValue(), "End");
            checkOverlap(startDateTime, endDateTime, customerID, -1);

            if (endDateTime.before(startDateTime)) {
                throw new RuntimeException("The end date cannot be before the start date.");
            }

            appointmentDAO.addAppointment(
                    TitleField.getText().trim(),
                    DescriptionField.getText().trim(),
                    LocationField.getText().trim(),
                    TypeField.getText().trim(),
                    startDateTime,
                    endDateTime,
                    user.getUserName(),
                    user.getUserName(),
                    customerID,
                    userID,
                    contactID
            );

            // Close add appointment scene
            Stage addAppointment = (Stage) AddSubmitButton.getScene().getWindow();
            addAppointment.close();

        } catch (SQLIntegrityConstraintViolationException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("ID Not Found", "The ID could not be matched in the database" + e.getMessage());
        } catch (SQLException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("SQL Error", "There was a problem communicating with the database " + e.getMessage());
        } catch (IllegalArgumentException integerException) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("Invalid Numeric Input", integerException.getMessage());
        } catch (RuntimeException fieldException) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            customAlert.genericAlert("Field Error", fieldException.getMessage());
        }
    }

    /**
     * Closes the appointment form when the cancel button is clicked.
     *
     * @param event The action event (Clicking on the cancel button).
     */
    @FXML
    void cancelAppointment(ActionEvent event) {
        // Close add appointment scene
        Stage addAppointment = (Stage) AppointmentField.getScene().getWindow();
        addAppointment.close();
    }


    //--------------------------------------Validation Methods--------------------------------------------------///
    /**
     * Validates that specific text fields contain valid integer values.
     *
     * @param completedField represents user input from a textFied in the appointment form
     * @param errorMessage the error message associated with the completedField that
     *                     should be displayed if validateInteger throws an exception
     *
     * @return The integer value if valid
     */
    private int validateInteger(String completedField, String errorMessage) {
        StringBuilder number = new StringBuilder();

        for (char c : completedField.toCharArray()) {
            if (Character.isDigit(c)) {
                number.append(c);
            } else {
                break;
            }
        }
        if (number.length() > 0) {
            return Integer.parseInt(number.toString());
        } else {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    /**
     * Checks if all required form fields are filled. throws exception if any are missing
     */
    private void validateRequiredFields() {
        String[] fields = getStringFields();
        for (String field : fields) {
            if (field == null || field.trim().isBlank()) {
                throw new RuntimeException("Please fill in all required fields before submitting.");
            }
        }
    }

    /**
     * Retrieves and returns input from text fields in the appointment form as a string array.
     *
     * @return An array of strings representing user input.
     * @throws RuntimeException if form fields are left blank
     */
    private String[] getStringFields() {
        // Initialize string array
        String[] stringFields = null;  //End DateTime Values
        try {
            // Collect user input in the appointment form
            stringFields = new String[]{UserField.getValue(), CustomerField.getValue(), ContactField.getValue(), //ID Values
                    TitleField.getText(), TypeField.getText(), LocationField.getText(), DescriptionField.getText(), //Appointment Type Values
                    String.valueOf(StartDateField.getValue()) ,StartTimeHR.getText(), StartTimeMin.getText(), //Start DateTime Values
                    String.valueOf(EndDateField.getValue()), EndTimeHR.getText(), EndTimeMin.getText()}; //End DateTime Values

            return stringFields;
        } catch (Exception e) {
            CustomAlerts customAlerts = new CustomAlerts(Alert.AlertType.WARNING);
            throw new RuntimeException("Please fill in all required fields before submitting");
        }

    }


    //--------------------------------------Helper & Utility Methods--------------------------------------------///

    /**
     * Checks if the given appointment time overlaps with any existing appointments for the customer.
     *
     * @param start Start timestamp of the appointment.
     * @param end End timestamp of the appointment.
     * @param customerID The ID of the customer.
     * @param appointmentID The ID of the appointment being edited (-1 for new appointments).
     * @throws SQLException If a database access error occurs.
     */
    private void checkOverlap(Timestamp start, Timestamp end, int customerID, int appointmentID) throws SQLException {
        String customerAppointments;

        // Checks if the appointment is existing or is new
        if (appointmentID == -1) {
            // Adding a new appointment (don't exclude any)
            customerAppointments = "SELECT * FROM appointments WHERE Customer_ID = ?";
        } else {
            // Modifying an existing appointment (exclude itself)
            customerAppointments = "SELECT * FROM appointments WHERE Customer_ID = ? AND Appointment_ID != ?";
        }

        DataQuery.setPreparedStatement(Main.conn, customerAppointments);
        // Initialize list
        ObservableList<Integer> conflictingAppointmentID = FXCollections.observableArrayList();

        // Get result set of appointments by customer and check for overlap
        try (PreparedStatement ps = DataQuery.getPreparedStatement()) {
            ps.setInt(1, customerID);
            if (appointmentID != -1) { // Existing appointment
                ps.setInt(2, appointmentID);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp existingStart = rs.getTimestamp("Start");
                    Timestamp existingEnd = rs.getTimestamp("End");

                    if (start.before(existingEnd) && end.after(existingStart)) {
                        conflictingAppointmentID.add(rs.getInt("Appointment_ID"));
                    }
                }
            }

            if (!conflictingAppointmentID.isEmpty()) {
                StringBuilder errorMessage = new StringBuilder("Conflicting Customer Appointment(s) found:\n");
                for (int id : conflictingAppointmentID) {
                    errorMessage.append("Appointment ID: ").append(id).append("\n");
                }
                throw new RuntimeException(errorMessage.toString());
            }

        } catch (SQLException e) {
            throw new SQLException("Database error in checkOverlap: " + e.getMessage(), e);
        }
    }

    /**
     * Formats a LocalDateTime object into a String representing its value in 12hr
     * and sets the AM/PM dropdown box in the appointment form to its correct value
     *
     * @param time The LocalDateTime to format.
     * @param startEnd Indicates if it's "start" or "end" time.
     * @return A formatted string representing the hour in 12-hour time.
     */
    private String formatTimeFields(LocalDateTime time, String startEnd) {
        StringBuilder hourValue = new StringBuilder();
        StringBuilder timeDesignation = new StringBuilder();
        DateTimeFormatter hourFormat = DateTimeFormatter.ofPattern("h a");
        String hour = time.format(hourFormat);

        // copy any digits in hour to hourValue
        // copy any non-digits (excluding whitespace) to timeDesignation
        for (char c : hour.toCharArray()) {
            if (Character.isDigit(c)) {
                hourValue.append(c);
            } else if (Character.isWhitespace(c)) {
                // This is an intentional no-op statement
                // No action is taken here
                ;
            } else {
                timeDesignation.append(c);
            }
        }
        // Set dorpdown box in form depending on if timeDesignation is am or pm
        if (timeDesignation.toString().equals("PM")) {
            if (startEnd.equals("start")) {
                StartAmPm.setValue("Pm");
            } else if (startEnd.equals("end")) {
                StartAmPm.setValue("Pm");
            }
        }
        // returns hourValue as a string to be set in the appointment form
        return hourValue.toString();
    }

    /**
     * Converts date and time components into a Timestamp.
     * Calls TimeHelper class to check if submitted time is within business hours
     *
     * @param date The selected date.
     * @param hourText Text from the hour input field.
     * @param minuteText Text from the minute input field.
     * @param amPm AM or PM selection.
     * @param fieldName A name for identifying the source (used for error messages).
     * @return A Timestamp representing the combined date and time.
     */
    private Timestamp getTimestamp(LocalDate date, String hourText, String minuteText, String amPm, String fieldName) {
        // Collect Hr and Min from text fields
        int hour = validateInteger(hourText, fieldName + " time must contain only numeric values.");
        int minute = validateInteger(minuteText, fieldName + " minutes must be between 0 and 59.");

        // Make sure hour and minute is within normal clock range
        if (minute < 0 || minute >= 60) {
            throw new RuntimeException(fieldName + " minutes must be between 0 and 59.");
        } else if (hour < 0 || hour > 12) {
            throw new RuntimeException(fieldName + "hours must be between 0 and 12");
        }
        // Converts 12 hour time to 24 hour time
        if ("Pm".equals(amPm) && hour < 12) {
            hour += 12;
        } else if ("Am".equals(amPm) && hour == 12) {
            hour = 0;  // Midnight case
        }

        // Convert date, hour, minute variables to a dateTime
        LocalDateTime dateTime = LocalDateTime.of(date, LocalTime.of(hour, minute));

        // Makes sure appointment isn't scheduled in the past
        if (dateTime.isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Appointment can't be scheduled in the past.\nPlease select a future date and time");
        }

        // Check if the time is within 8:00 AM - 10:00 PM (EST)
        if (!TimeHelper.compareWithBusinessHours(dateTime)) {
            throw new RuntimeException(fieldName + " time must be between 8:00 AM and 10:00 PM (EST).");
        }

        // Returns timestamp with users values
        return Timestamp.valueOf(date.atTime(LocalTime.of(hour, minute)));
    }


    /**
     * Toggles between views depending on whether the user is adding or modifying an appointment.
     *
     * @param viewChoice True if add view is selected, false for modify view.
     */
    @FXML
    void viewType (Boolean viewChoice) {
        this.addView = viewChoice;

        if (viewChoice) {
            AppointmentField.setText("Auto Gen.");
            AddAppLabel.setVisible(true);
            AddAppImg.setVisible(true);
            ModAppLabel.setVisible(false);
            ModAppImg.setVisible(false);
            AddSubmitButton.setVisible(true);
            ModSubmitButton.setVisible(false);

        } else if (!viewChoice) {
            AddAppLabel.setVisible(false);
            AddAppImg.setVisible(false);
            ModAppLabel.setVisible(true);
            ModAppImg.setVisible(true);
            AddSubmitButton.setVisible(false);
            ModSubmitButton.setVisible(true);
        }

    }

}
