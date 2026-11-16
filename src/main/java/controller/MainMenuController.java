package controller;

import Util.DAO.AppointmentDAOImp;
import Util.DAO.CountryDivisionDAOImp;
import Util.DAO.CustomersDAOImp;
import com.student.appointment_scheduler.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Controller class for managing the main dashboard of the application.
 * It handles navigation between different views, CRUD operations, user management,
 * searching, table selection, and logout functionality.
 */
public class MainMenuController implements Initializable {

    //----------------------------------------------FXML Elements----------------------------------------------//
    // Buttons
    @FXML
    private Button AddAppointmentButton;

    @FXML
    private Button DelAppointmentButton;

    @FXML
    private Button ModAppointmentButton;

    @FXML
    private Button addCustButton;

    @FXML
    private Button delCustButton;

    @FXML
    private Button modCustButton;

    @FXML
    private Button report1;

    @FXML
    private Button report2;

    @FXML
    private Button report3;

    // Labels
    @FXML
    private Label AllCustomersLabel;

    @FXML
    private Label AppointmentLabel;

    @FXML
    private Label CustomerLabel;

    @FXML
    private Label LogoutLabel;

    @FXML
    private Label ReportsLabel;

    @FXML
    private Label UserLabel;

    @FXML
    private Label MonthlyLabel;

    @FXML
    private Label monthlyLabelTab;

    @FXML
    private Label WeeklyLabel;

    @FXML
    private Label weeklyLabelTab;

    @FXML
    private Label AllLabel;

    @FXML
    private Label allLabelTab;

    // Text Fields
    @FXML
    private TextField AppointmentSearch;

    @FXML
    private TextField CustomerSearch;

    // Containers
    @FXML
    private AnchorPane AllAppView;

    @FXML
    private AnchorPane AppointmentPane;

    @FXML
    private AnchorPane CustomerTopPane;

    @FXML
    private AnchorPane CustomerView;

    @FXML
    private AnchorPane MonthlyView;

    @FXML
    private AnchorPane WeeklyView;

    @FXML
    private AnchorPane ReportsView;

    @FXML
    private HBox AppointmentButtons;

    @FXML
    private HBox AppointmentTabs;

    @FXML
    private HBox CustomerButtons;


    // Highlights (Rectangles)
    @FXML
    private Rectangle AllHighlight;

    @FXML
    private Rectangle MonthlyHighlight;

    @FXML
    private Rectangle WeeklyHighlight;


    // ---------------------- Table Views ------------------------
    // All Appointment table
    @FXML
    private TableView<Appointment> AllAppointmentsTable;
    @FXML private TableColumn<Appointment, Integer> idColumn;
    @FXML private TableColumn<Appointment, String> titleColumn;
    @FXML private TableColumn<Appointment, String> descriptionColumn;
    @FXML private TableColumn<Appointment, String> locationColumn;
    @FXML private TableColumn<Appointment, Integer> contactColumn;
    @FXML private TableColumn<Appointment, String> typeColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> startColumn;
    @FXML private TableColumn<Appointment, LocalDateTime> endColumn;
    @FXML private TableColumn<Appointment, Integer> customerColumn;
    @FXML private TableColumn<Appointment, Integer> userColumn;

    //Customer Table
    @FXML
    private TableView<Customer> CustomerTable;
    @FXML private TableColumn<Customer, Integer> customerIDCol;
    @FXML private TableColumn<Customer, String> customerNameCol;
    @FXML private TableColumn<Customer, String> customerAddressCol;
    @FXML private TableColumn<Customer, String> customerZipCol;
    @FXML private TableColumn<Customer, String> customerPhoneCol;
    @FXML private TableColumn<Customer, String> customerCountryCol;
    @FXML private TableColumn<Customer, Integer> customerDivisionCol;

    //Weekly Table
    @FXML
    private TableView<Appointment> WeeklyTable;
    @FXML private TableColumn<Appointment, Integer> weekIDCol;
    @FXML private TableColumn<Appointment, String> weekTitleCol;
    @FXML private TableColumn<Appointment, String> weekDescriptionCol;
    @FXML private TableColumn<Appointment, String> weekLocationCol;
    @FXML private TableColumn<Appointment, String> weekTypeCol;
    @FXML private TableColumn<Appointment, Integer> weekContactCol;
    @FXML private TableColumn<Appointment, LocalDateTime> weekStartCol;
    @FXML private TableColumn<Appointment, LocalDateTime> weekEndCol;
    @FXML private TableColumn<Appointment, Integer> weekCustomerCol;
    @FXML private TableColumn<Appointment, Integer> weekUserCol;


    //Monthly Table
    @FXML
    private TableView<Appointment> MonthlyTable;
    @FXML private TableColumn<Appointment, Integer> monthIDCol;
    @FXML private TableColumn<Appointment, String> monthTitleCol;
    @FXML private TableColumn<Appointment, String> monthDescriptionCol;
    @FXML private TableColumn<Appointment, String> monthLocationCol;
    @FXML private TableColumn<Appointment, String> monthContactCol;
    @FXML private TableColumn<Appointment, String> monthTypeCol;
    @FXML private TableColumn<Appointment, LocalDateTime> monthStartCol;
    @FXML private TableColumn<Appointment, LocalDateTime> monthEndCol;
    @FXML private TableColumn<Appointment, Integer> monthCustomerCol;
    @FXML private TableColumn<Appointment, Integer> monthUserCol;


    //--------------------------------------------Instant Variables--------------------------------------------//
    private Connection conn;
    private Appointment selectedAppointment = null;
    private Customer selectedCustomer = null;
    private User currentUser = null;
    private static Stage userCardStage = null;
    @FXML
    ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    @FXML
    ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    @FXML
    ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    @FXML
    ObservableList<Customer> customerList = FXCollections.observableArrayList();

    //--------------------------------Main Menu Controller Initialization---------------------------------------//
    /**
     * Initializes the appointments table by setting column values
     * and loads database data to all the ObservableLists
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Initialize data access objects
        AppointmentDAOImp appointmentsDAO = new AppointmentDAOImp(Main.conn);
        CustomersDAOImp customersDAO = new CustomersDAOImp(Main.conn);

        //Checks to see if controller should load MainMenu or UserCard configurations
        if (AllAppView != null) {
            try {
                //Fill the appropriate ObservableLists with data from the database
                allAppointments = appointmentsDAO.getAllAppointments();
                weeklyAppointments = appointmentsDAO.getWeeklyAppointments(LocalDateTime.now());
                monthlyAppointments = appointmentsDAO.getMonthlyAppointments(LocalDateTime.now());
                customerList = customersDAO.getAllCustomers();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            setLabels();
            initializeCustomerTable();
            initializeWeeklyTable();
            initializeMonthlyTable();
            initializeAppointmentsTable();
            allAppView();

            /**
             * Schedules a request to set focus on the AllAppointmentsTable after all pending UI events have been processed.
             * This ensures that the table receives focus safely within the JavaFX Application Thread, The Platform.runLater method defers execution
             * of the provided Runnable until the current events are handled.
             */
            Platform.runLater(() -> AllAppointmentsTable.requestFocus());

        }
    }

    /**
     * Initializes the Appointment-Table by setting column values
     * and loads row data from the allAppointment list
     */
    @FXML
    private void initializeAppointmentsTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("ContactID"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("MeetingType"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("UserID"));

        AllAppointmentsTable.setItems(allAppointments);
    }

    /**
     * Initializes the Monthly table by setting column values
     * and loads row data from the monthlyAppointment list
     */
    @FXML
    private void initializeMonthlyTable() {
        monthIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        monthTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        monthDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        monthLocationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        monthTypeCol.setCellValueFactory(new PropertyValueFactory<>("MeetingType"));
        monthStartCol.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        monthEndCol.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        monthCustomerCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        monthUserCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        monthContactCol.setCellValueFactory(new PropertyValueFactory<>("ContactID"));

        MonthlyTable.setItems(monthlyAppointments);
    }

    /**
     * Initializes the Weekly table by setting column values
     * and loads row data from the weeklyAppointment list
     */
    @FXML
    private void initializeWeeklyTable() {
        weekIDCol.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        weekTitleCol.setCellValueFactory(new PropertyValueFactory<>("Title"));
        weekDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("Description"));
        weekLocationCol.setCellValueFactory(new PropertyValueFactory<>("Location"));
        weekTypeCol.setCellValueFactory(new PropertyValueFactory<>("MeetingType"));
        weekStartCol.setCellValueFactory(new PropertyValueFactory<>("StartTime"));
        weekEndCol.setCellValueFactory(new PropertyValueFactory<>("EndTime"));
        weekCustomerCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        weekUserCol.setCellValueFactory(new PropertyValueFactory<>("UserID"));
        weekContactCol.setCellValueFactory(new PropertyValueFactory<>("ContactID"));

        WeeklyTable.setItems(weeklyAppointments);
    }

    /**
     * Initializes the Customer-Table by setting column values
     * and loads row data from the customer list
     */
    @FXML
    private void initializeCustomerTable() {
        customerIDCol.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("CustomerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("Address"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        customerZipCol.setCellValueFactory(new PropertyValueFactory<>("Zip"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("DivisionID"));

        /**
         * customerCountryCol.setCellValueFactory(lambda expression);
         * Sets the cell value factory for the customerCountryCol column using a lambda expression.
         *
         * This lambda expression dynamically determines the country name to display in the
         * "Country" column of the Customer Table based on the customer's division ID.
         *
         * The lambda expression receives each Customer object
         * then calculates the country name.
         */
        customerCountryCol.setCellValueFactory(cellData -> {
                    Customer c = cellData.getValue();
                    int divNum = c.getDivisionID();
                    String country;

                    if (divNum < 55) {
                        country = "America";
                    } else if (divNum > 100) {
                        country = "United Kingdom";
                    } else {
                        country = "Canada";
                    }

                    return new SimpleStringProperty(country);
                });

        // Set table items to customerList
        CustomerTable.setItems(customerList);
    }




    //--------------------------------------View Navigation Methods---------------------------------------------//
    /**
     * Navigates to the all appointments view.
     */
    @FXML //All Appointments view no parameters
    private void allAppView(){
        // Active elements
        AllAppView.setVisible(true);
        AllLabel.setVisible(true);
        AppointmentButtons.setVisible(true);
        AllHighlight.setVisible(true);
        AppointmentPane.setVisible(true);
        AppointmentTabs.setVisible(true);

        // Inactive elements
        CustomerButtons.setVisible(false);
        CustomerTopPane.setVisible(false);
        CustomerView.setVisible(false);
        MonthlyLabel.setVisible(false);
        MonthlyHighlight.setVisible(false);
        MonthlyView.setVisible(false);
        ReportsView.setVisible(false);
        WeeklyHighlight.setVisible(false);
        WeeklyLabel.setVisible(false);
        WeeklyView.setVisible(false);

        // Refresh Elements
        AppointmentSearch.clear();
        selectedAppointment = null;
        initializeAppointmentsTable();
    }

    /**
     * Navigates to the all appointments view with event handling.
     *
     * @param event The mouse event (Clicking on all appointments icon/label).
     */
    @FXML //All Appointments View
    void allAppView(MouseEvent event) {

        // Active elements
        AllAppView.setVisible(true);
        AllLabel.setVisible(true);
        AppointmentButtons.setVisible(true);
        AllHighlight.setVisible(true);
        AppointmentPane.setVisible(true);
        AppointmentTabs.setVisible(true);

        // Inactive elements
        CustomerButtons.setVisible(false);
        CustomerTopPane.setVisible(false);
        CustomerView.setVisible(false);
        MonthlyLabel.setVisible(false);
        MonthlyHighlight.setVisible(false);
        MonthlyView.setVisible(false);
        ReportsView.setVisible(false);
        WeeklyHighlight.setVisible(false);
        WeeklyLabel.setVisible(false);
        WeeklyView.setVisible(false);

        // Refresh Elements
        AppointmentSearch.clear();
        selectedAppointment = null;
        initializeAppointmentsTable();

    }

    /**
     * Navigates to the monthly appointments view.
     *
     * @param event The mouse event (Clicking on monthly appointments).
     */
    @FXML //Monthly Appointments View
    void monthlyAppView(MouseEvent event) {


        // Active elements
        AppointmentButtons.setVisible(true);
        AppointmentPane.setVisible(true);
        MonthlyLabel.setVisible(true);
        MonthlyView.setVisible(true);
        MonthlyHighlight.setVisible(true);

        // Inactive elements
        AllAppView.setVisible(false);
        AllHighlight.setVisible(false);
        AllLabel.setVisible(false);
        CustomerButtons.setVisible(false);
        CustomerTopPane.setVisible(false);
        CustomerView.setVisible(false);
        WeeklyHighlight.setVisible(false);
        WeeklyLabel.setVisible(false);
        WeeklyView.setVisible(false);

        // Refresh Elements
        AppointmentSearch.clear();
        selectedAppointment = null;
        initializeMonthlyTable();
    }

    /**
     * Navigates to the weekly appointments view.
     *
     * @param event The mouse event (Clicking on weekly appointments).
     */
    @FXML //Weekly Appointments View
    void weeklyAppView(MouseEvent event) {
        // Active elements
        AppointmentButtons.setVisible(true);
        AppointmentPane.setVisible(true);
        WeeklyHighlight.setVisible(true);
        WeeklyLabel.setVisible(true);
        WeeklyView.setVisible(true);

        // Inactive elements
        AllAppView.setVisible(false);
        AllHighlight.setVisible(false);
        AllLabel.setVisible(false);
        CustomerButtons.setVisible(false);
        CustomerTopPane.setVisible(false);
        CustomerView.setVisible(false);
        MonthlyHighlight.setVisible(false);
        MonthlyLabel.setVisible(false);
        MonthlyView.setVisible(false);

        // Refresh Elements
        AppointmentSearch.clear();
        selectedAppointment = null;
        initializeWeeklyTable();

    }

    /**
     * Navigates to the all customers view.
     *
     * @param event The mouse event (Clicking on customers icon/label).
     */
    @FXML
    void allCustView(MouseEvent event) {
        // Active elements
        AllCustomersLabel.setVisible(true);
        CustomerButtons.setVisible(true);
        CustomerTopPane.setVisible(true);
        CustomerView.setVisible(true);

        // Inactive elements
        AllAppView.setVisible(false);
        AppointmentButtons.setVisible(false);
        AppointmentPane.setVisible(false);
        MonthlyView.setVisible(false);
        ReportsView.setVisible(false);
        WeeklyView.setVisible(false);

        // Refresh Elements
        CustomerSearch.clear();
        selectedCustomer = null;
        selectedAppointment = null;
        AppointmentSearch.clear();
        initializeCustomerTable();
    }

    /**
     * Navigates to the all reports view.
     *
     * @param event The mouse event (Clicking on reports icon/label).
     */
    @FXML
    void allReportsView(MouseEvent event) {
        //Active elements
        ReportsView.setVisible(true);

        // Inactive elements
        AllAppView.setVisible(false);
        AppointmentButtons.setVisible(false);
        AppointmentPane.setVisible(false);
        CustomerButtons.setVisible(false);
        CustomerTopPane.setVisible(false);
        CustomerView.setVisible(false);
        MonthlyView.setVisible(false);
        WeeklyView.setVisible(false);
    }

    /**
     * Launches Report 1 view.
     *
     * @param actionEvent The action event (Clicking Report1 button).
     * @throws IOException  If an input or output error occurs.
     * @throws SQLException If a database access error occurs.
     */
    @FXML
    public void launchReport1 (ActionEvent actionEvent) throws IOException, SQLException {
        // Load Scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/Report1.fxml"));
        Parent root = loader.load();

        // Pass Data to report1 controller
        ReportController1 controller = loader.getController();
        controller.setConn(this.conn);
        controller.queryReport();

        // Show stage
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Launches Report 2 view.
     *
     * @param actionEvent The action event (Clicking Report2 button).
     * @throws IOException  If an input or output error occurs.
     * @throws SQLException If a database access error occurs.
     */
    @FXML
    public void launchReport2 (ActionEvent actionEvent) throws IOException, SQLException {
        // Load Scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/Report2.fxml"));
        Parent root = loader.load();

        // Pass Data to report2 controller
        ReportController2 controller = loader.getController();
        controller.setConn(this.conn);
        controller.queryReport();

        // Show stage
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    /**
     * Launches Report 3 view.
     *
     * @param actionEvent The action event (Clicking Report3 button).
     * @throws IOException If an input or output error occurs.
     */
    @FXML
    public void launchReport3 (ActionEvent actionEvent) throws IOException {
        // Load Scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/Report3.fxml"));
        Parent root = loader.load();

        // Load Controller
        ReportController3 controller = loader.getController();

        // Show stage
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }


    //--------------------------------------CRUD Operation Methods----------------------------------------------//
    /**
     * Opens the "Add Appointment" form.
     *
     * @param event The action event (clicking the add appointment button).
     * @throws IOException If an input or output error occurs.
     */
    @FXML
    void addAppLaunch (ActionEvent event) throws IOException {
        // Load Scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/AddAppointment.fxml"));
        Parent root = loader.load();

        // Pass Data to appointment controller
        AppointmentController appointmentController = loader.getController();
        appointmentController.viewType(true);
        appointmentController.userData(currentUser);

        // Show stage
        Stage addAppointmentStage = new Stage();
        addAppointmentStage.setScene(new Scene(root));
        addAppointmentStage.initModality(Modality.APPLICATION_MODAL);
        addAppointmentStage.showAndWait();

        // Reinitialize tables and lists after any possible changes
        initialize(null, null);

    }

    /**
     * Opens the "Modify Appointment" form.
     *
     * @param event The action event (clicking the modify appointment button).
     * @throws IOException If an input or output error occurs.
     */
    @FXML
    void modAppLaunch(ActionEvent event) throws IOException {
        // If empty appointment selection display alert, else load scene
        if (selectedAppointment == null) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Empty Selection", "No appointment selected to modify\n" +
                    Main.messages.getString("modify_error"));
        } else {
            // Load Scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/AddAppointment.fxml"));
            Parent root = loader.load();
            // Pass Data to appointment controller
            AppointmentController appointmentController = loader.getController();
            appointmentController.viewType(false);
            appointmentController.userData(currentUser);
            appointmentController.setAppointment(selectedAppointment);
            // Show stage
            Stage addAppointmentStage = new Stage();
            addAppointmentStage.setScene(new Scene(root));
            addAppointmentStage.initModality(Modality.APPLICATION_MODAL);
            addAppointmentStage.showAndWait();
            // Reinitialize tables and lists after any possible changes
            initialize(null, null);
        }


    }

    /**
     * Deletes the selected appointment.
     *
     * @param event The action event (clicking the delete appointment button).
     */
    @FXML
    void delAppLaunch(ActionEvent event) {
        //Check for selected appointment, else display selection error alert
        if (selectedAppointment != null) {
            try {
                AppointmentDAOImp appointmentsDAO = new AppointmentDAOImp(Main.conn);
                CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
                boolean confirmation = customAlert.returnTrueFalse("Confirm Deletion",
                        Main.messages.getString("delete_appointment")+ "\n" +
                                "Appointment ID: " + selectedAppointment.getAppointmentID() +
                                "\nAppointment Title: " + selectedAppointment.getTitle());
                // If user clicked yes delete appointment
                if (confirmation) {
                    appointmentsDAO.deleteAppointment(selectedAppointment.getAppointmentID());
                    CustomAlerts a = new CustomAlerts(Alert.AlertType.INFORMATION);
                    a.genericAlert("Appointment Deleted", "The Appointment: \n\n" + "ID: " + selectedAppointment.getAppointmentID() +
                            "\nType: " + selectedAppointment.getMeetingType() +
                            "\n Has been deleted");
                }
            } catch (SQLException e) {
                CustomAlerts b = new CustomAlerts(Alert.AlertType.ERROR);
                b.genericAlert("SQL Error", Main.messages.getString("sql_appointment_error"));
            } catch (Exception e) {
                CustomAlerts c = new CustomAlerts(Alert.AlertType.ERROR);
                c.genericAlert("Error", Main.messages.getString("appointment_error"));
            }
            // Reinitialize tables and lists after any possible changes
            initialize(null, null);

        } else {
            // Display empty appointment selection alert
            CustomAlerts nullSelection = new CustomAlerts(Alert.AlertType.ERROR);
            nullSelection.genericAlert("Empty Selection", Main.messages.getString("empty_appointment_error"));
        }


    }

    /**
     * Opens the "Add Customer" form.
     *
     * @param event The action event (clicking the add customer button).
     * @throws IOException If an input or output error occurs.
     */
    @FXML
    void addCustLaunch(ActionEvent event) throws IOException {
        // Load Scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/AddCustomer.fxml"));
        Parent root = loader.load();
        // Pass Data to customer controller
        CustomerController customerController = loader.getController();
        customerController.addView();
        customerController.setUser(currentUser);
        // Show stage
        Stage addCustomerStage = new Stage();
        addCustomerStage.setScene(new Scene(root));
        addCustomerStage.initModality(Modality.APPLICATION_MODAL);
        addCustomerStage.showAndWait();
        // Reinitialize tables and lists after any possible changes
        initialize(null, null);
    }

    /**
     * Opens the "Modify Customer" form.
     *
     * @param event The action event (clicking the modify customer button).
     * @throws IOException If an input or output error occurs.
     */
    @FXML
    void modCustLaunch(ActionEvent event) throws IOException {
        // Display alert if no customer selected, else launch modify customer scene
        if (selectedCustomer == null) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Empty Selection", Main.messages.getString("customer_selection_error") +
                    "\n" +
                    Main.messages.getString("customer_selection_error2"));
        } else {
            // Load Scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/AddCustomer.fxml"));
            Parent root = loader.load();
            // Pass Data to customer controller
            CustomerController custController = loader.getController();
            custController.setUser(currentUser);
            custController.setSelectedCustomer(selectedCustomer);
            custController.modifyView();
            // Show stage
            Stage addCustomerStage = new Stage();
            addCustomerStage.setScene(new Scene(root));
            addCustomerStage.initModality(Modality.APPLICATION_MODAL);
            addCustomerStage.showAndWait();
            // Reinitialize tables and lists after any possible changes
            initialize(null, null);
        }
    }

    /**
     * Deletes the selected customer.
     *
     * @param event The action event (Clicking the delete customer button).
     */
    @FXML
    void delCustLaunch(ActionEvent event) {
        // If no customer selected display alert, else delete customer and customer-appointments
        if (selectedCustomer != null) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.WARNING);
            if (customAlert.returnTrueFalse("Delete Customer?", Main.messages.getString("customer_delete") +
                    "\n\nSelected Customer : " + selectedCustomer.getCustomerName() +
                    "\nCustomer ID: " + selectedCustomer.getCustomerID() +
                    "\n \n" + Main.messages.getString("customer_delete2"))) {


                try {//Delete Customer and their appointments
                    //Remove any associated appointments
                    AppointmentDAOImp appointmentDAO = new AppointmentDAOImp(Main.conn);
                    appointmentDAO.deleteAssociatedAppointments(selectedCustomer.getCustomerID());
                    //Remove Customer
                    CustomersDAOImp customerDAO = new CustomersDAOImp(Main.conn);
                    customerDAO.deleteCustomer(selectedCustomer.getCustomerID());
                    // Alert customer of successful deletion
                    customAlert.genericAlert("Customer Deleted", Main.messages.getString("customer_deleted"));

                } catch (SQLException e) {
                    customAlert.genericAlert("SQL Error", Main.messages.getString("sql_customer_error"));
                }
            }
            // Reinitialize all tables and lists to reflect changes
            initialize(null, null);
        } else {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("Empty Customer Selection", Main.messages.getString("selection_customer_error"));
        }
    }


    //--------------------------------------User Management Methods---------------------------------------------//
    /**
     * Launches the User Card view.
     *
     * @param event The mouse event (Clicking the user icon/label).
     */
    @FXML
    void userCardLaunch(MouseEvent event) {
        // Allows only one instant of the window to show at a time
        if (userCardStage != null && userCardStage.isShowing()) {
            userCardStage.toFront(); // Bring existing window to front
            return;
        }

        try { // Load and show User Card Stage
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/UserCard.fxml"));
            Parent root = loader.load();

            UserCardController userCardControllerController = loader.getController();
            userCardControllerController.setUser(currentUser);

            userCardStage = new Stage();
            userCardStage.setScene(new Scene(root));
            userCardStage.show();

            userCardStage.setOnCloseRequest(e -> userCardStage = null); // Reset when closed

        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace for debugging
            throw new RuntimeException(e);
        }

    }


    //--------------------------------------Selection Methods---------------------------------------------------//
    /**
     * Handles row selection in a generic table.
     *
     * @param event The mouse event (Clicking a row in the All-Appointments table).
     */
    @FXML
    void rowSelection(MouseEvent event) {
        // Users selected row in the All-Appointments table
        selectedAppointment = AllAppointmentsTable.getSelectionModel().getSelectedItem();


    }

    /**
     * Handles row selection in the weekly appointments table.
     *
     * @param event The mouse event (Clicking a row in the Weekly-Appointments table).
     */
    @FXML
    void rowSelectionWeekly(MouseEvent event) {
        // Users selected row in the Weekly-Appointment table
        selectedAppointment = WeeklyTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Handles row selection in the monthly appointments table.
     *
     * @param event The mouse event (Clicking a row in the Monthly-Appointments table).
     */
    @FXML
    void rowSelectionMonthly(MouseEvent event) {
        // Users selected row in the Monthly-Appointment Table
        selectedAppointment = MonthlyTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Handles row selection in the customer table.
     *
     * @param event The mouse event (Clicking a row in the Customers table).
     */
    @FXML
    void rowSelectionCustomer(MouseEvent event) {
        // users selected row in the Customers table
        selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();
    }


    //--------------------------------------Search Methods------------------------------------------------------//
    /**
     * Initiates an appointment search based on user input.
     *
     * @param event The key event (Typing in the appointment search box).
     */
    @FXML
    public void startAppSearch (KeyEvent event) {
        // Get users search parameters
        String query = AppointmentSearch.getText().trim().toLowerCase();

        //List of matching appointments
        ObservableList<Appointment> matchingAppointments = FXCollections.observableArrayList();

        // Search results displayed in the Monthly-appointments table
        if (MonthlyHighlight.isVisible()) {//Search by Appointment title, Meeting type, Appointment/customerID. Consider changing it to search for fewer values
            for (Appointment appointment : monthlyAppointments) {
                // Check if query matches Title or Appointment ID
                if (appointment.getMeetingType().contains(query)  || String.valueOf(appointment.getAppointmentID()).contains(query)) {
                    //Add appointment to list if query matches appointmentID or meeting type
                    matchingAppointments.add(appointment);
                }
            }
            MonthlyTable.setItems(matchingAppointments);
            return;
        } else if (WeeklyHighlight.isVisible()) {
            for (Appointment appointment : weeklyAppointments) {
                // Check if query matches Title or Appointment ID
                if (appointment.getMeetingType().contains(query)  || String.valueOf(appointment.getAppointmentID()).contains(query)) {
                    //Add appointment to list if query matches appointmentID or meeting type
                    matchingAppointments.add(appointment);
                }
            }
            WeeklyTable.setItems(matchingAppointments);
            return;
            //Searches display in the All-Appointments table
        } else if (AllHighlight.isVisible()){
            for (Appointment appointment : allAppointments) {
                // Check if query matches Title or Appointment ID
                if (appointment.getMeetingType().contains(query)  || String.valueOf(appointment.getAppointmentID()).contains(query)) {
                    //Add appointment to list if query matches appointmentID or meeting type
                    matchingAppointments.add(appointment);
                }
            }
            AllAppointmentsTable.setItems(matchingAppointments);
            return;
        }
    }

    /**
     * Initiates a customer search based on user input.
     * Searches are by customer name or customer ID
     *
     * @param event The key event (Typing in the customer search box).
     */
    @FXML
    void startCustSearch(KeyEvent event) {
        // Users Search
        String query = CustomerSearch.getText().trim().toLowerCase();

        // List of matching Customers
        ObservableList<Customer> matchingCustomer = FXCollections.observableArrayList();

        // If searchbar is blank show all results
        if (CustomerSearch.getText().isBlank()) {
            CustomerTable.setItems(customerList);
            return;
        } else {
            for (Customer customer : customerList) {;
                // Check if query matches Name or Customer ID
                if (customer.getCustomerName().toLowerCase().contains(query) || String.valueOf(customer.getCustomerID()).contains(query)) {
                    matchingCustomer.add(customer);
                }
            }
            // Set customer table to matching search items
            CustomerTable.setItems(matchingCustomer);
            return;
        }
    }


    //--------------------------------------Logout & Exit Methods-----------------------------------------------//
    /**
     * Confirms logout action and redirects to the login screen.
     *
     * @param event The mouse event (Clicking on the Logout icon/label).
     * @throws IOException If an input or output error occurs.
     */
    @FXML
    void confirmLogout(MouseEvent event) throws IOException {
        CustomAlerts customAlerts = new CustomAlerts(Alert.AlertType.WARNING);
        // True if customer clicks yes
        if(customAlerts.returnTrueFalse("Confirm Logout?", Main.messages.getString("logout_prompt"))){
            // Close login scene
            Stage loginStage = (Stage) AllCustomersLabel.getScene().getWindow();
            loginStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/student/views/LoginScene.fxml"));
            Parent root = loader.load();

            LoginController controller = loader.getController();
            controller.setConnection(conn); // Pass connection

            //Show login scene
            loginStage.setScene(new Scene(root));
            loginStage.show();
        }
    }


    //--------------------------------------Set Data Methods----------------------------------------------------//
    /**
     * Sets the database connection for executing queries.
     *
     * @param passedConnection The connection to be set.
     */
    public void setConn(Connection passedConnection){
        this.conn = passedConnection;
    }

    /**
     * Sets labels and UI components with relevant information.
     * uses a resource bundle to display the users language in the program
     */
    @FXML
    private void setLabels() {
        addCustButton.setText(Main.messages.getString("AddCustButton"));
        AddAppointmentButton.setText(Main.messages.getString("AddAppointmentButton"));
        AllCustomersLabel.setText(Main.messages.getString("AllCustomersLabel"));
        AllLabel.setText(Main.messages.getString("AllLabel"));
        allLabelTab.setText(Main.messages.getString("allLabelTab"));
        AppointmentLabel.setText(Main.messages.getString("AppointmentLabel"));
        AppointmentSearch.setPromptText(Main.messages.getString("AppointmentSearch"));
        CustomerLabel.setText(Main.messages.getString("CustomerLabel"));
        CustomerSearch.setPromptText(Main.messages.getString("CustomerSearch"));
        DelAppointmentButton.setText(Main.messages.getString("DelAppointmentButton"));
        delCustButton.setText(Main.messages.getString("delCustButton"));
        LogoutLabel.setText(Main.messages.getString("LogoutLabel"));
        ModAppointmentButton.setText(Main.messages.getString("ModAppointmentButton"));
        modCustButton.setText(Main.messages.getString("modCustButton"));
        MonthlyLabel.setText(Main.messages.getString("MonthlyLabel"));
        monthlyLabelTab.setText(Main.messages.getString("monthlyLabelTab"));
        report1.setText(Main.messages.getString("report1"));
        report2.setText(Main.messages.getString("report2"));
        report3.setText(Main.messages.getString("report3"));
        ReportsLabel.setText(Main.messages.getString("ReportsLabel"));
        UserLabel.setText(Main.messages.getString("UserLabel"));
        WeeklyLabel.setText(Main.messages.getString("WeeklyLabel"));
        weeklyLabelTab.setText(Main.messages.getString("weeklyLabelTab"));
    }

    /**
     * Sets the current user to the controller.
     *
     * @param user The logged-in user.
     */
    public void setUser(User user) {
        this.currentUser = user;
    }

}
