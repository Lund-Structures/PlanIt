package controller;


import Util.DAO.CountryDAOImp;
import Util.DAO.CountryDivisionDAOImp;
import Util.DAO.CustomersDAOImp;
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
import java.net.URL;
import java.sql.SQLException;
import java.util.FormatterClosedException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CustomerController implements Initializable {

    //----------------------------------------------FXML Elements----------------------------------------------//

    // Buttons
    @FXML
    private Button AddCustButton;

    @FXML
    private Button CancelButton;

    @FXML
    private Button ModCustButton;

    // ImageViews
    @FXML
    private ImageView AddCustImg;

    @FXML
    private ImageView ModCustImg;

    // Labels
    @FXML
    private Label AddCustLabel;

    @FXML
    private Label ModCustLabel;

    //Text Fields
    @FXML
    private TextField AddressField;

    @FXML
    private TextField IDField;

    @FXML
    private TextField NameField;

    @FXML
    private TextField PhoneField;

    @FXML
    private TextField PostalField;

    // Combo Boxes
    @FXML
    private ComboBox<String> CountryField;

    @FXML
    private ComboBox<String> StateField;


    //----------------------------------------------Instant Variables--------------------------------------------//

    // Lists
    ObservableList<Country> countriesList = FXCollections.observableArrayList();
    ObservableList<CountryDivision> firstDivision =FXCollections.observableArrayList();
    // Objects
    User currentUser;
    Customer selectedCustomer;

    //-----------------------------------Customer Controller Initialization-------------------------------------//
    /**
     * Initializes the controller after its root element has been completely processed.
     * This method is automatically called, It is used to populate combo boxes and prepare the
     * UI components for user interaction.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param resourceBundle the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeFields();
    }

    /**
     * Initializes UI ComboBoxes with data from the database for countries and divisions.
     * Displays a database error alert if data transfer fails.
     */
    private void initializeFields() {


        try {

            CountryDAOImp countryDAO = new CountryDAOImp(Main.conn);
            countriesList = countryDAO.getAllCountries();

            CountryDivisionDAOImp divisionDAO = new CountryDivisionDAOImp(Main.conn);
            firstDivision = divisionDAO.getAllDivisions();

            for (Country c : countriesList) {

                CountryField.getItems().add(String.valueOf(c.getCountryID()) + ")  " + c.getCountry());
            }
        } catch (SQLException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Database Error", "Couldn't auto populate combo boxes from the database");
        }
    }


    //--------------------------------------View Setup Methods--------------------------------------------------//
    /**
     * Configures the form UI for adding a new customer.
     * Sets default values and hides modify-specific elements.
     */
    @FXML
    void addView() {
        AddCustButton.setVisible(true);
        AddCustImg.setVisible(true);
        AddCustLabel.setVisible(true);

        ModCustImg.setVisible(false);
        ModCustImg.setVisible(false);
        ModCustLabel.setVisible(false);
        CountryField.setPromptText("Select Country");
        StateField.setPromptText("Select Country First");
    }

    /**
     * Configures the form UI and fields to display the modify customer view.
     * Pre-populates fields with selected customer data.
     */
    @FXML
    void modifyView() {

        CountryDivisionDAOImp countryDivisionDAO = new CountryDivisionDAOImp(Main.conn);
        Country customerCountry = countryDivisionDAO.getCountry(selectedCustomer.getDivisionID());
        String customerState = "No Division Found";
        for(CountryDivision d : firstDivision) {
            if(d.getDivisionID() == selectedCustomer.getDivisionID()) {
                customerState = d.getName();
            }
        }
        StateField.getItems().add("Reselect from the country dropdown to see states");


        AddCustButton.setVisible(false);
        AddCustImg.setVisible(false);
        AddCustLabel.setVisible(false);
        AddCustButton.setVisible(false);

        ModCustImg.setVisible(true);
        ModCustImg.setVisible(true);
        ModCustLabel.setVisible(true);
        ModCustButton.setVisible(true);

        IDField.setText(String.valueOf(selectedCustomer.getCustomerID()));
        NameField.setText(selectedCustomer.getCustomerName());
        PhoneField.setText(selectedCustomer.getPhone());
        AddressField.setText(selectedCustomer.getAddress());
        PostalField.setText(selectedCustomer.getZip());
        CountryField.setValue(customerCountry.getCountryID() + ") " + customerCountry.getCountry());
        StateField.setValue(selectedCustomer.getDivisionID() + ") " + customerState);
        System.out.println("Mod view after setting value: " + StateField.getValue());
    }

    /**
     * Updates the division ComboBox options when a country is selected.
     * @param event the ActionEvent triggered by selecting a country.
     */
    @FXML
    void setDivisionBox(ActionEvent event) {
        String country = CountryField.getValue();
        int countryID = 0;
        // Sets division dropdown based on countryID
        if(country != null) {
            String number = String.valueOf(country.toCharArray()[0]);
            countryID = Integer.parseInt(number);
            populateStateField(countryID);
        }

    }

    /**
     * Populates the state ComboBox with divisions corresponding to the selected country.
     * @param countryID the ID of the selected country.
     */
    @FXML
    void populateStateField(int countryID) {
        // Clear stateField before repopulating
        StateField.getItems().clear();
        // Declare StateList
        ObservableList<CountryDivision> stateList;
        CountryDivisionDAOImp divisionDAO = new CountryDivisionDAOImp(Main.conn);
        try{ // Fill stateList based on countryID
            stateList = divisionDAO.getDivision(countryID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (CountryDivision state : stateList) {
            StateField.getItems().add(state.getDivisionID() + ") " + state.getName());
        }

    }

    /**
     * Sets the current user in the controller.
     * @param user The currently logged-in user.
     */
    void setUser (User user) {
        this.currentUser = user;
    }

    /**
     * Sets the selected customer object to modify.
     * @param selectedCustomer the Customer selected from the previous view.
     */
    @FXML
    void setSelectedCustomer(Customer selectedCustomer) {
        this.selectedCustomer = selectedCustomer;
    }


    //-------------------------------------Customer CRUD Methods------------------------------------------------//
    /**
     * Closes the customer form window.
     * @param event the ActionEvent triggered by clicking the Cancel button.
     */
    @FXML
    void cancelCustomer(ActionEvent event) {
        // Close Customer form
        Stage customer = (Stage) CancelButton.getScene().getWindow();
        customer.close();
    }

    /**
     * Updates an existing customer's data after validating input fields.
     * Handles SQL exceptions, phone formatting errors, and validation issues.
     * @param event the ActionEvent triggered by clicking the Modify button.
     */
    @FXML
    void modifyCustomer(ActionEvent event) {
        try{ // Validate textFields and uploads data to the database
            validateRequiredFields();
            CustomersDAOImp customerDAO = new CustomersDAOImp(Main.conn);
            customerDAO.updateCustomer(
                    Integer.parseInt(IDField.getText()),
                    NameField.getText().trim(),
                    AddressField.getText().trim(),
                    PostalField.getText().trim(),
                    formatPhoneNumber(PhoneField.getText().trim()),
                    formatDivisionID(StateField.getValue()),
                    currentUser.getUserName()
            );
            // Save customer name for alert
            String name = NameField.getText().trim();

            // Alerts user of sucessful submission
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Customer Modified", "The customer : " + name +
                    "\nWas successfully modified");

            // Close form if submission to database was successful
            Stage addCustomer = (Stage) AddCustLabel.getScene().getWindow();
            addCustomer.close();


        } catch (SQLException e) {
            System.out.println(e.getMessage()); //MANAGE CODE!!!!!!!!
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("SQL Error", "There was a problem submitting the customer to the database");
        } catch (FormatterClosedException phoneFormatError) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("Invalid Phone Number", "Phone number entries should contain 8 - 12 numbers");
        } catch (RuntimeException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("Field Error", e.getMessage());
        }
    }

    /**
     * Submits a new customer to the database after validating fields and formatting.
     * Displays appropriate error messages based on failure points.
     * @param event the ActionEvent triggered by clicking the Submit button.
     */
    @FXML
    void submitCustomer(ActionEvent event) {


        try{
            validateRequiredFields();
            // Retrieve customer from form and submit it to the database
            CustomersDAOImp customerDAO = new CustomersDAOImp(Main.conn);
            customerDAO.addCustomer(
                    NameField.getText().trim(),
                    AddressField.getText().trim(),
                    formatPostal(PostalField.getText().trim()),
                    formatPhoneNumber(PhoneField.getText().trim()),
                    currentUser.getUserName(),
                    currentUser.getUserName(),
                    formatDivisionID(StateField.getValue())

            );

            // Close stage if submission was successful
            Stage addCustomer = (Stage) AddCustLabel.getScene().getWindow();
            addCustomer.close();

            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Customer Added", "Customer: " + NameField.getText().trim() + "\nAdded Successfully");

        } catch (SQLException e) {
            System.out.println(e.getMessage()); //MANAGE CODE!!!!!!!!
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("SQL Error", "There was a problem submitting the customer to the database");
        } catch (FormatterClosedException phoneFormatError) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("Invalid Phone Number", "Phone number entries should contain 10 - 12 numbers");
        } catch (RuntimeException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("Field Error", e.getMessage());
        }
    }


    //--------------------------------------Validation & Formatting---------------------------------------------//
    /**
     * Validates that all required fields are filled in and not blank.
     * Throws a RuntimeException if any required field is missing.
     */
    private void validateRequiredFields() {
        String[] stringFields = null;
        try { // Retrieve all user entered data in textFields
            stringFields = new String[]{NameField.getText(), PhoneField.getText(), AddressField.getText(), CountryField.getValue(), StateField.getValue(), PostalField.getText()};

        } catch (Exception e) {
            throw new RuntimeException("Please fill in all required fields before submitting");
        }
        // Checks for blank fields
        for (String field : stringFields) {
            if (field == null || field.trim().isBlank()) {
                throw new RuntimeException("Please fill in all required fields before submitting.");
            }
        }
    }

    /**
     * Formats the input phone number by inserting dashes.
     * @param phoneNumber the input phone number string.
     * @return the formatted phone number.
     * @throws FormatterClosedException if the phone number length is not valid.
     */
    private String formatPhoneNumber(String phoneNumber) throws FormatterClosedException {
        // Remove all non-numeric characters
        String cleanNumber = phoneNumber.replaceAll("[^0-9]", "");
        StringBuilder formatted = new StringBuilder(cleanNumber);
        // Formats phone numbers with length 10, 11, and 12 numbers
        if (formatted.length() == 10) {
            // Format: XXX-XXX-XXXX
            formatted.insert(3, "-").insert(7, "-");
        } else if (cleanNumber.length() == 11) {
            // Format: X-XXX-XXX-XXXX
            formatted.insert(1, "-").insert(5, "-").insert(9, "-");
        } else if (cleanNumber.length() == 12) {
            // Format: XX-XXX-XXX-XXXX
            formatted.insert(2, "-").insert(6, "-").insert(10, "-");
        } else {
            throw new FormatterClosedException();
        }

        // return formatted string
        return formatted.toString();
    }

    /**
     * Validates the postal code format.
     * @param postalCode the user input postal code.
     * @return cleaned postal code if valid.
     */
    private String formatPostal(String postalCode) {
        String cleanPostal = postalCode.replaceAll("[^0-9], [^A-Z], [^a-z],", "");
        StringBuilder formatted = new StringBuilder(cleanPostal);

        if(formatted.length() == 5) {
            return formatted.toString();
        } else {
            throw new RuntimeException("Unrecgonized Postal Code.\n\n postal code requires 5 alphanumeric-only characters");
        }

    }

    /**
     * Retrieves the division ID from the selected division name.
     * @param division the name of the division selected.
     * @return division ID matching the name.
     */
    private int getDivisionID (String division) {
        // Finds matching division name in firstDivision List
        for (CountryDivision d : firstDivision) {
            if (division.equals(d.getName())) {
                return d.getDivisionID();
            }
        }
        throw new RuntimeException("Couldn't find division in list ");
    }

    private int formatDivisionID (String division) {
        Pattern pattern = Pattern.compile("^(.*?)\\)");
        Matcher matcher = pattern.matcher(division);
        int divisionID = 0;

        if (matcher.find()) {
           divisionID = Integer.parseInt(matcher.group(1));

        }
        return divisionID;
    }






























}

