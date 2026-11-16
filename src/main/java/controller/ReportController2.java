package controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Report2;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * The ReportController2 class handles the functionality for displaying
 * and interacting with the report view of the application.
 * It loads a report with details about appointments, including their associated
 * contact information, and binds the data to a TableView.
 * */
public class ReportController2 implements Initializable {

    //----------------------------------------------FXML Elements----------------------------------------------//
    @FXML
    private ScrollPane scrollPane;

    // Report2 Table and Columns
    @FXML
    private TableView<Report2> ReportTable2;
    @FXML
    private TableColumn<Report2, Integer> appointmentID;
    @FXML
    private TableColumn<Report2, Integer> customerColumn;
    @FXML
    private TableColumn<Report2, String> descriptionColumn;
    @FXML
    private TableColumn<Report2, LocalDateTime> endColumn;
    @FXML
    private TableColumn<Report2, String> nameColumn;
    @FXML
    private TableColumn<Report2, LocalDateTime> startColumn;
    @FXML
    private TableColumn<Report2, String> titleColumn;

    //--------------------------------------------Instant Variables--------------------------------------------//
    // Database connection and data list
    Connection conn;
    ObservableList<Report2> reportList = FXCollections.observableArrayList();

    //--------------------------------------Report2 Controller Initialization------------------------------------//
    /**
     * Initializes the controller class.
     *
     * @param url the location of the FXML file
     * @param resourceBundle the resources used for localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Before initialization is called queryReport is called which then calls initializeCustomerTable

        // Ensure the scrollPane starts at the top of the page.
        //This lambda expression takes no arguments, the expression represents a runnable task
        Platform.runLater(() -> scrollPane.setVvalue(0));
    }

    /**
     * Initializes the report table and sets rows to the reportList
     */
    @FXML
    private void initializeReportTable() {

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("ContactName"));
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("AppointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        startColumn.setCellValueFactory(new PropertyValueFactory<>("Start"));
        endColumn.setCellValueFactory(new PropertyValueFactory<>("End"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("CustomerID"));

        ReportTable2.setItems(reportList);
    }


    //--------------------------------------Report2 Data Retrieval-----------------------------------------------//
    /**
     * Queries the database to fetch report data for appointments and contacts.
     * The data is displayed in the report table.
     *
     * @throws SQLException if a database access error occurs
     */
    @FXML
    public void queryReport() throws SQLException {
        //Disables ONLY_FULL_GROUP_BY to acquire report2 data
        Statement stmt = conn.createStatement();
        stmt.execute("SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';");

        String reportQuery = "SELECT " +
                                "c.Contact_ID, " +
                                "c.Contact_Name, " +
                                "a.Appointment_ID, " +
                                "a.Title, " +
                                "a.Type, " +
                                "a.Description, " +
                                "a.Start AS Start_DateTime, " +
                                "a.End AS End_DateTime, " +
                                "a.Customer_ID " +
                             "FROM " +
                                "appointments a " +
                             "JOIN " +
                                "contacts c ON a.Contact_ID = c.Contact_ID " +
                             "ORDER BY " +
                                "c.Contact_ID, a.Start;";


        // Execute query and populate the reportList with data
        try (PreparedStatement ps = conn.prepareStatement(reportQuery);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int contactId = rs.getInt("Contact_ID");
                String contactName = rs.getString("Contact_Name");
                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime startDateTime = rs.getTimestamp("Start_DateTime").toLocalDateTime();
                LocalDateTime endDateTime = rs.getTimestamp("End_DateTime").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");

                Report2 report = new Report2(contactId,
                                            contactName,
                                            appointmentId,
                                            title,
                                            type,
                                            description,
                                            startDateTime,
                                            endDateTime,
                                            customerId
                );
                //Add new report to report list
                reportList.add(report);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initializeReportTable();
    }


    //----------------------------------------Set Methods-------------------------------------------------------//
    /**
     * Sets the database connection to be used for querying the report.
     * @param incomingConn the database connection to set
     */
    public void setConn(Connection incomingConn) {
        this.conn = incomingConn;
    }


}
