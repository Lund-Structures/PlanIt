package Util.DAO;

import Util.DataQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointment;
import model.CustomAlerts;
import java.sql.*;
import java.time.*;
import java.time.temporal.TemporalAdjusters;

/** AppointmentDAO Implementation class of AppointmentDAO interface.
 * This class allows for the retrieval of all appointment records from the database.
 */
public class AppointmentDAOImp implements AppointmentDAO {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    //Appointment Lists
    ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructor method to instantiate AppointmentDAOImp.
     * @param conn sets the connection
     */
    public AppointmentDAOImp(Connection conn) {
        this.conn = conn;
    }

    //-------------------------------Retrieve Appointments Methods-------------------------------------//
    /** {@inheritDoc} */
    @Override
    public Appointment getAppointment(int appointmentID) throws SQLException {
        String sql = "Select * From " +
                        "appointments " +
                     "WHERE " +
                        "Appointment_ID = ?";

        //Query and populate Result Set with appointment
        DataQuery.setPreparedStatement(conn, sql);
        ps = DataQuery.getPreparedStatement();
        ps.setInt(1, appointmentID);
        rs = ps.executeQuery();

        if (rs.next()) {
           Appointment a = new Appointment(
                rs.getInt("Appointment_ID"),
                rs.getString("Title"),
                rs.getString("Description"),
                rs.getString("Location"),
                rs.getString("Type"),
                rs.getTimestamp("Start").toLocalDateTime(),
                rs.getTimestamp("End").toLocalDateTime(),
                rs.getTimestamp("Create_Date").toLocalDateTime(),
                rs.getString("Created_By"),
                rs.getTimestamp("Last_Update").toLocalDateTime(),
                rs.getString("Last_Updated_By"),
                rs.getInt("Customer_ID"),
                rs.getInt("User_ID"),
                rs.getInt("Contact_ID")
           );
           return a;
        }
        else {
            System.out.println("No appointment found");
            return null;
        }

    }

    /** {@inheritDoc} */
    @Override
    public ObservableList<Appointment> getAllAppointments() throws SQLException {
        //SQL Statement
        String sql = "Select * From appointments";


        //Query and populate Result Set with appointments
        DataQuery.setPreparedStatement(conn, sql);
        ps = DataQuery.getPreparedStatement();
        rs = ps.executeQuery();

        //Add each appointment in result set to appointments list
        while(rs.next()) {
            int id = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            LocalDateTime created = rs.getTimestamp("Create_Date").toLocalDateTime();
            String createdBy = rs.getString("Created_By");
            LocalDateTime updated = rs.getTimestamp("Last_Update").toLocalDateTime();
            String updatedBy = rs.getString("Last_Updated_By");
            int customer = rs.getInt("Customer_ID");
            int user = rs.getInt("User_ID");
            int contact = rs.getInt("Contact_ID");

            //Add appointments to Observable List
            appointments.add(new Appointment(id, title, description, location, type, start, end, created, createdBy, updated, updatedBy, customer, user, contact));
        }

        //Return list of appointments
        return appointments;

    }


    /**
     * Retrieves all appointments that fall within the specified monthly time interval.
     *
     * @param timeInterval the time interval for the month
     * @return a list of appointments within the specified month
     * @throws SQLException if a database error occurs
     */
    public ObservableList<Appointment> getMonthlyAppointments(LocalDateTime timeInterval) throws SQLException {

        //SQL statement
        String monthlySelect = "SELECT * FROM " +
                                    "appointments " +
                                "WHERE " +
                                    "Start BETWEEN ? AND ?";

        // Get full month range
        LocalDateTime startOfMonth = timeInterval.withDayOfMonth(1).with(LocalTime.MIN);
        LocalDateTime endOfMonth = timeInterval.with(TemporalAdjusters.lastDayOfMonth()).with(LocalTime.MAX);

        //Sets and executes SQL statement
        try (PreparedStatement ps = conn.prepareStatement(monthlySelect)) {
            ps.setTimestamp(1, Timestamp.valueOf(startOfMonth));
            ps.setTimestamp(2, Timestamp.valueOf(endOfMonth));
            rs = ps.executeQuery();

                //Add result set appointments to monthly appointment list
                while (rs.next()) {
                    monthlyAppointments.add(new Appointment(
                            rs.getInt("Appointment_ID"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getString("Location"),
                            rs.getString("Type"),
                            rs.getTimestamp("Start").toLocalDateTime(),
                            rs.getTimestamp("End").toLocalDateTime(),
                            rs.getTimestamp("Create_Date").toLocalDateTime(),
                            rs.getString("Created_By"),
                            rs.getTimestamp("Last_Update").toLocalDateTime(),
                            rs.getString("Last_Updated_By"),
                            rs.getInt("Customer_ID"),
                            rs.getInt("User_ID"),
                            rs.getInt("Contact_ID")
                    ));
                }
            }

        //Return List of appointments
        return monthlyAppointments;
    }

    /**
     * Retrieves all appointments that fall within the specified weekly time interval.
     *
     * @param timeInterval the time interval for the week
     * @return a list of appointments within the specified week
     * @throws SQLException if a database error occurs
     */
    public ObservableList<Appointment> getWeeklyAppointments(LocalDateTime timeInterval) throws SQLException {

        //SQL statement
        String weeklySelect = "SELECT * FROM " +
                                "appointments " +
                            "WHERE " +
                                "Start BETWEEN ? AND ?";

        // Get start of the week (Monday 00:00:00) and end of the week (Sunday 23:59:59)
        LocalDateTime startOfWeek = timeInterval.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)).with(LocalTime.MIN);
        LocalDateTime endOfWeek = timeInterval.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY)).with(LocalTime.MAX);

        //Sets and executes SQL statement
        try (PreparedStatement ps = conn.prepareStatement(weeklySelect)) {
            ps.setTimestamp(1, Timestamp.valueOf(startOfWeek));
            ps.setTimestamp(2, Timestamp.valueOf(endOfWeek));
            ResultSet rs = ps.executeQuery();

                while (rs.next()) {
                    weeklyAppointments.add(new Appointment(
                            rs.getInt("Appointment_ID"),
                            rs.getString("Title"),
                            rs.getString("Description"),
                            rs.getString("Location"),
                            rs.getString("Type"),
                            rs.getTimestamp("Start").toLocalDateTime(),
                            rs.getTimestamp("End").toLocalDateTime(),
                            rs.getTimestamp("Create_Date").toLocalDateTime(),
                            rs.getString("Created_By"),
                            rs.getTimestamp("Last_Update").toLocalDateTime(),
                            rs.getString("Last_Updated_By"),
                            rs.getInt("Customer_ID"),
                            rs.getInt("User_ID"),
                            rs.getInt("Contact_ID")
                    ));
                }
            }
        //Return List of appointments
        return weeklyAppointments;
    }



    //------------------------------Override Methods: CRUD Operations------------------------------------------//
    /** {@inheritDoc} */
    @Override
    public void addAppointment(String title, String description, String location, String type, Timestamp startDateTime, Timestamp endDateTime, String createdBy, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException {
        //SQL statement
        String insertAppointment = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, NOW(),? ,NOW() , ?, ?, ?, ?)";

        //Sets and executes SQL statement
        try (PreparedStatement ps = conn.prepareStatement(insertAppointment)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, startDateTime);
            ps.setTimestamp(6, endDateTime);
            ps.setString(7, createdBy);
            ps.setString(8, lastUpdatedBy);
            ps.setInt(9, customerID);
            ps.setInt(10, userID);
            ps.setInt(11, contactID);

            ps.executeUpdate();

            //Display Alert informing of added appointment
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Appointment Added", "The Appointment:\n\n" +
                                                                "Title: " + title +
                                                                "\nStart: " + startDateTime.toLocalDateTime() +
                                                                "\n\nHas Been Created");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void updateAppointment(int appointmentID, String title, String description, String location, String type, Timestamp startDateTime, Timestamp endDateTime, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException {
        //SQL statement
        String updateAppointment = "UPDATE " +
                                        "appointments " +
                                    "SET " +
                                        "Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?," +
                                        "Last_Update = NOW(), Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                                    "WHERE " +
                                        "Appointment_ID = ?";

        //Sets and executes SQL statement
        try (PreparedStatement ps = conn.prepareStatement(updateAppointment)) {
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setTimestamp(5, startDateTime);
            ps.setTimestamp(6, endDateTime);
            ps.setString(7, lastUpdatedBy);
            ps.setInt(8, customerID);
            ps.setInt(9, userID);
            ps.setInt(10, contactID);
            ps.setInt(11, appointmentID);

            ps.executeUpdate();

            //Display Alert informing of updated appointment
            CustomAlerts customAlert = new CustomAlerts(Alert.AlertType.INFORMATION);
            customAlert.genericAlert("Appointment Updated", "The Appointment:\n\n" +
                    "Title: " + title +
                    "\nStart: " + startDateTime.toLocalDateTime() +
                    "\n\nHas Been Updated");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void deleteAppointment(int appointmentID) throws SQLException {
        //SQL statement
        String deleteAppointment = "DELETE FROM " +
                                        "appointments " +
                                    "WHERE " +
                                        "Appointment_ID = ?";

        //Sets and executes SQL statement
        try (PreparedStatement ps = conn.prepareStatement(deleteAppointment)) {
            ps.setInt(1, appointmentID);

            ps.executeUpdate();

        }
    }

    /** {@inheritDoc} */
    @Override
    public void deleteAssociatedAppointments(int customerID) throws SQLException {
        //SQL statement
        String deleteCustomerAppointments = "DELETE FROM appointments WHERE Customer_ID = ?";

        //Sets and executes SQL statement
        try (PreparedStatement ps = conn.prepareStatement(deleteCustomerAppointments)) {
            ps.setInt(1, customerID);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
