package controller;

import Util.DataQuery;
import com.student.appointment_scheduler.Main;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The ReportController3 class is responsible for managing the report view that displays a pie chart
 * showing the distribution of customers by region (Division) from the database. It retrieves the data
 * from the database, processes it, and populates a pie chart with the data.
 */
public class ReportController3 implements Initializable {

    //----------------------------------------------FXML Elements----------------------------------------------//
    // The PieChart component in the UI that displays the distribution of customers by region.
    @FXML
    private PieChart regionChart;


    //--------------------------------------------Instant Variables--------------------------------------------//
    ObservableList<String> regionList = FXCollections.observableArrayList();
    ObservableList<Integer> customerCount = FXCollections.observableArrayList();


    //--------------------------------------Report3 Controller Initialization------------------------------------//
    /**
     * Initializes the controller by retrieving the pie chart data and setting up the pie chart.
     *
     * It calls the getPieData() method to populate the data lists and creates the pie chart data.
     * Tooltips are also added to show the region name and customer count when hovering over pie chart slices.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is unknown.
     * @param resourceBundle The resources used to localize the controller, or null if no localization is needed.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Call to getPieData runs a SQL query that populates the dataLists
        getPieData();
        // Pie data list holds a combo of data from the lists that were filled in the call to getPieData()
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (int i = 0; i < regionList.size(); ++i) {
            pieData.add(new PieChart.Data(regionList.get(i), customerCount.get(i)));
        }

        // Set data for pie chart
        regionChart.setData(pieData);

        // Attach tooltips after setting data. tooltips show country name and number of customers
        // Iterates through each data in the pieData. "data" represents an individual item from pieData collection
        pieData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName(), " Customers: ", data.pieValueProperty())));

    }


    //--------------------------------------Report3 Data Retrieval-----------------------------------------------//
    /**
     * Retrieves the customer count data from the database and populates the regionList and customerCount lists.
     * This method constructs and executes a SQL query to join customer and division tables,
     */
    private void getPieData() {
        String getCount = "SELECT " +
                            "c.Division_ID, " +
                            "d.Division AS Division_Name, " +
                            "COUNT(*) AS Customer_Count " +
                          "FROM " +
                            "customers c " +
                          "JOIN " +
                            "first_level_divisions d ON c.Division_ID = d.Division_ID " +
                          "GROUP BY " +
                            "c.Division_ID, d.Division";

        try {
            DataQuery.setPreparedStatement(Main.conn, getCount);
            ResultSet rs = DataQuery.getPreparedStatement().executeQuery();

            while (rs.next()) {
                String name = rs.getString("Division_Name");
                regionList.add(name);

                int count = rs.getInt("Customer_Count");
                customerCount.add(count);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

}
