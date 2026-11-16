package Util.DAO;

import javafx.collections.ObservableList;
import model.Appointment;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * The AppointmentDAO interface provides method definitions
 * for retrieving appointment records from the database.
 */
public interface AppointmentDAO {
    /**
     * getAppointment. Returns a specific appointment specified by userID.
     * @param appointmentID Integer
     * @return Appointment
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public Appointment getAppointment(int appointmentID) throws SQLException;

    /**
     * getAllAppointments. Returns all appointments in the database as an observable list typed with Appointment.
     * @return ObservableList<Appointment>Messages
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public ObservableList<Appointment> getAllAppointments() throws SQLException;

    /**
     * addAppointment. Responsible for adding an appointment to the database.
     * @param title String
     * @param description String
     * @param location String
     * @param type String
     * @param startDateTime LocalDateTime
     * @param endDateTime LocalDateTime
     * @param createdBy String
     * @param lastUpdatedBy String
     * @param customerID Integer
     * @param userID Integer
     * @param contactID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public void addAppointment(String title, String description, String location, String type, Timestamp startDateTime, Timestamp endDateTime, String createdBy, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException;

    /**
     * updateAppointment. Updates a selected appointment and adds it to the database.
     * @param appointmentID Integer
     * @param title String
     * @param description String
     * @param location String
     * @param type String
     * @param startDateTime LocalDateTime
     * @param endDateTime LocalDateTime
     * @param lastUpdatedBy String
     * @param customerID Integer
     * @param userID Integer
     * @param contactID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public void updateAppointment(int appointmentID, String title, String description, String location, String type, Timestamp startDateTime, Timestamp endDateTime, String lastUpdatedBy, int customerID, int userID, int contactID) throws SQLException;

    /**
     * deleteAppointment. Deletes a specific appointment by appointment ID.
     * @param appointmentID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public void deleteAppointment(int appointmentID) throws SQLException;

    /**
     * deleteAssociatedAppointments. Deletes all appointments that are associated with a customer.
     * @param customerID Integer
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public void deleteAssociatedAppointments(int customerID) throws SQLException;
}
