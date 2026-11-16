package controller;

import Util.DataQuery;
import com.student.appointment_scheduler.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.CustomAlerts;
import model.Report1;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Controller for the first report view.
 * This class handles querying the database and displaying report data in a TableView.
 */
public class ReportController1 implements Initializable {

    //----------------------------------------------FXML Elements----------------------------------------------//
    @FXML
    private ScrollPane scrollPane;

    /** TableView displaying report data. */
    @FXML
    private TableView<Report1> ReportTable1;
    @FXML private TableColumn<Report1, String> monthColumn;
    @FXML private TableColumn<Report1, Integer> typeColumn;
    @FXML private TableColumn<Report1, String> totalColumn;

    //--------------------------------------------Instant Variables--------------------------------------------//
    //List to store report data
    private final ObservableList<Report1> reportList = FXCollections.observableArrayList();
    private Connection conn;


    //--------------------------------------Report1 Controller Initialization------------------------------------//
    /**
     * Initializes the controller.
     * This method ensures the scroll pane starts at the top of the page.
     *
     * @param url The location used to resolve relative paths for the root object.
     * @param resourceBundle The resources used to localize the root object.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Ensure the scrollPane starts at the top of the page.
        //This lambda expression takes no arguments, the expression represents a runnable task
        Platform.runLater(() -> scrollPane.setVvalue(0));
    }

    /**
     * Initializes the customer report table.
     */
    @FXML
    private void initializeCustomerTable() {
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("Month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("Type"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("Total"));

        ReportTable1.setItems(reportList);
    }


    //--------------------------------------Report1 Data Retrieval-----------------------------------------------//
    /**
     * Queries the database to retrieve report data and populates the table.
     * The query retrieves the count of appointments grouped by month and type.
     *
     * @throws SQLException If a database error occurs.
     */
    @FXML
    public void queryReport() throws SQLException {
        //Disables ONLY_FULL_GROUP_BY to acquire report1 data
        Statement stmt = conn.createStatement();
        stmt.execute("SET SESSION sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'");

        //SQL statement
        String reportQuery = "SELECT " +
                                "MONTHNAME(Start) AS Month, " +
                                "Type, " +
                                "COUNT(*) AS Total_Appointments " +
                            "FROM appointments " +
                            "GROUP BY MONTH(Start), Type " +
                            "ORDER BY MONTH(Start), Type";

        try { //Run query and save it to a result set
            DataQuery.setPreparedStatement(Main.conn, reportQuery);
            PreparedStatement ps = DataQuery.getPreparedStatement();
            ResultSet rs = ps.executeQuery();

            // Populate report list with query results
            while (rs.next()) {
                reportList.add(new Report1(
                        rs.getString(1),  // Month
                        rs.getString(2),  // Type
                        rs.getInt(3)      // Total appointments
                ));
            }
        } catch (SQLException e) {
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.ERROR);
            customAlert.genericAlert("SQL Error", "There was a problem communicating with the database.");
            System.out.println("SQL Error: " + e.getMessage());
        }
        //Sets Customer report table to all items in the reportList
        initializeCustomerTable();
    }


    //----------------------------------------Set Methods-------------------------------------------------------//
    /**
     * Sets the database connection.
     *
     * @param incomingConn The database connection to be used by this controller.
     */
    public void setConn(Connection incomingConn) {
        this.conn = incomingConn;
    }
}