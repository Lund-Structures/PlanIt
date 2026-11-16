package model;

import java.time.LocalDateTime;

/**
 * Represents a detailed report entry for an appointment, including contact, appointment details, and timing information.
 */
public class Report2 {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private final int contactID;
    private final String contactName;
    private final int appointmentID;
    private final String title;
    private final String type;
    private final String description;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final int customerID;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a Report2 object with the specified contact ID, contact name, appointment details, description, and time range.
     *
     * @param contactID the ID of the contact associated with the appointment
     * @param contactName the name of the contact
     * @param appointmentID the unique ID of the appointment
     * @param title the title of the appointment
     * @param type the type of appointment
     * @param description the description of the appointment
     * @param start the start time of the appointment
     * @param end the end time of the appointment
     * @param customerID the ID of the customer associated with the appointment
     */
    public Report2(int contactID, String contactName, int appointmentID, String title, String type, String description, LocalDateTime start, LocalDateTime end, int customerID) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.appointmentID = appointmentID;
        this.title = title;
        this.type = type;
        this.description = description;
        this.start = start;
        this.end = end;
        this.customerID = customerID;
    }


    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Returns the ID of the customer associated with the appointment.
     * @return the customer ID
     */
    public int getCustomerID() {return customerID;}

    /**
     * Returns the end time of the appointment.
     * @return the end time of the appointment
     */
    public LocalDateTime getEnd() {return end;}

    /**
     * Returns the start time of the appointment.
     * @return the start time of the appointment
     */
    public LocalDateTime getStart() {return start;}

    /**
     * Returns the description of the appointment.
     * @return the appointment description
     */
    public String getDescription() {return description;}

    /**
     * Returns the type of the appointment.
     * @return the appointment type
     */
    public String getType() {return type;}

    /**
     * Returns the title of the appointment.
     * @return the appointment title
     */
    public String getTitle() {return title;}

    /**
     * Returns the unique ID of the appointment.
     * @return the appointment ID
     */
    public int getAppointmentID() {return appointmentID;}

    /**
     * Returns the name of the contact associated with the appointment.
     * @return the contact name
     */
    public String getContactName() {return contactName;}

    /**
     * Returns the ID of the contact associated with the appointment.
     * @return the contact ID
     */
    public int getContactID() {return contactID;}

    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Set methods for this class are intentionally left blank.
     * This section may be populated in the future if needed.
     */
}