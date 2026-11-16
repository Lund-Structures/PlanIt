package Util.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementation of the CustomersDAO interface for accessing customer records from the database.
 * Provides CRUD operations for the 'customers' table.
 */
public class CustomersDAOImp implements CustomersDAO {

    //--------------------------------------------Instant Variable--------------------------------------------//
    private final Connection conn;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs the DAO with an active database connection.
     *
     * @param incomingConn the database connection
     */
    public CustomersDAOImp(Connection incomingConn) {
        this.conn = incomingConn;
    }
    //---------------------------------Retrieve All Customers Method-------------------------------------------//
    /** {@inheritDoc} */
    @Override
    public ObservableList<Customer> getAllCustomers() throws SQLException {
        ObservableList<Customer> customersDAOList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customers";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Customer customer = new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Division_ID")
                );
                customersDAOList.add(customer);
            }
        }
        return customersDAOList;
    }


    //------------------------------Override Methods: CRUD Operations------------------------------------------//
    /** {@inheritDoc} */
    @Override
    public void addCustomer(String customerName, String address, String zipcode, String phone,
                            String createdBy, String lastUpdatedBy, int divisionID) throws SQLException {

        String addCustomer = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, " +
                "Create_Date, Created_By, Last_Update, Last_updated_By, Division_ID) " +
                "VALUES (?, ?, ?, ?, NOW(), ?, NOW(), ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(addCustomer)) {
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, zipcode);
            ps.setString(4, phone);
            ps.setString(5, createdBy);
            ps.setString(6, lastUpdatedBy);
            ps.setInt(7, divisionID);
            ps.executeUpdate();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateCustomer(int customerID, String customerName, String address, String zipcode,
                               String phone, int divisionID, String lastUpdatedBy) throws SQLException {

        String updateCustomer = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, " +
                "Phone = ?, Last_Update = NOW(), Last_updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateCustomer)) {
            ps.setString(1, customerName);
            ps.setString(2, address);
            ps.setString(3, zipcode);
            ps.setString(4, phone);
            ps.setString(5, lastUpdatedBy);
            ps.setInt(6, divisionID);
            ps.setInt(7, customerID);
            ps.executeUpdate();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deleteCustomer(int customerID) throws SQLException {
        String deleteCustomer = "DELETE FROM customers WHERE Customer_ID = ?";

        try (PreparedStatement ps = conn.prepareStatement(deleteCustomer)) {
            ps.setInt(1, customerID);
            ps.executeUpdate();
        }
    }

    /** {@inheritDoc} */
    @Override
    public Customer getCustomer(int customerID) throws SQLException {
        String selectCustomer = "SELECT * FROM customers WHERE Customer_ID = ?";

        try (PreparedStatement ps = conn.prepareStatement(selectCustomer)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("Customer_ID"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        rs.getString("Phone"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
                        rs.getString("Last_Updated_By"),
                        rs.getInt("Division_ID")
                );
            }
        }

        return null;
    }
}