package model;

import java.time.LocalDateTime;

/**
 * Represents an appointment in the scheduling system.
 * <p>
 * This class holds information about an appointment, including its details,
 * scheduling times, and associations with customers, users, and contacts.
 * </p>
 */
public class Appointment {

    //--------------------------------------------Instant Variables--------------------------------------------//
    /** Unique identifier for the appointment. */
    private int appointmentID;

    /** Title of the appointment. */
    private String title;

    /** Description of the appointment. */
    private String description;

    /** Location where the appointment takes place. */
    private String location;

    /** Type or category of the appointment. */
    private String meetingType;

    /** Start date and time of the appointment. */
    private LocalDateTime startTime;

    /** End date and time of the appointment. */
    private LocalDateTime endTime;

    /** Date and time when the appointment was created. */
    private LocalDateTime createDate;

    /** User who created the appointment. */
    private String createdBy;

    /** Date and time of the last update to the appointment. */
    private LocalDateTime lastUpdate;

    /** User who last updated the appointment. */
    private String lastUpdatedBy;

    /** Unique identifier for the associated customer. */
    private int customerID;

    /** Unique identifier for the associated user. */
    private int userID;

    /** Unique identifier for the associated contact. */
    private int contactID;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs an Appointment object with the specified details.
     *
     * @param appointmentID The unique ID of the appointment.
     * @param title The title of the appointment.
     * @param description The description of the appointment.
     * @param location The location of the appointment.
     * @param type The type or category of the appointment.
     * @param startDateTime The start time of the appointment.
     * @param endDateTime The end time of the appointment.
     * @param createDate The creation date of the appointment.
     * @param createdBy The user who created the appointment.
     * @param lastUpdate The timestamp of the last update.
     * @param lastUpdatedBy The user who last updated the appointment.
     * @param customerID The ID of the associated customer.
     * @param userID The ID of the associated user.
     * @param contactID The ID of the associated contact.
     */
    public Appointment(int appointmentID, String title, String description, String location, String type,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, LocalDateTime createDate, String createdBy,
                       LocalDateTime lastUpdate, String lastUpdatedBy, int customerID, int userID, int contactID) {
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.meetingType = type;
        this.startTime = startDateTime;
        this.endTime = endDateTime;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.customerID = customerID;
        this.userID = userID;
        this.contactID = contactID;
    }


    //-----------------------------------------------Get Methods-----------------------------------------------//
    /** @return The contact ID associated with the appointment. */
    public int getContactID() { return contactID; }

    /** @return The user ID associated with the appointment. */
    public int getUserID() { return userID; }

    /** @return The customer ID associated with the appointment. */
    public int getCustomerID() { return customerID; }

    /** @return The name of the user who last updated the appointment. */
    public String getLastUpdatedBy() { return lastUpdatedBy; }

    /** @return The timestamp of the last update to the appointment. */
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    /** @return The name of the user who created the appointment. */
    public String getCreatedBy() { return createdBy; }

    /** @return The timestamp when the appointment was created. */
    public LocalDateTime getCreateDate() { return createDate; }

    /** @return The end time of the appointment. */
    public LocalDateTime getEndTime() { return endTime; }

    /** @return The start time of the appointment. */
    public LocalDateTime getStartTime() { return startTime; }

    /** @return The type or category of the appointment. */
    public String getMeetingType() { return meetingType; }

    /** @return The location of the appointment. */
    public String getLocation() { return location; }

    /** @return The title of the appointment. */
    public String getTitle() { return title; }

    /** @return The unique appointment ID. */
    public int getAppointmentID() { return appointmentID; }

    /** @return The description of the appointment. */
    public String getDescription() { return description; }



    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Sets the contact ID.
     * @param contactID The new contact ID.
     */
    public void setContactID(int contactID) { this.contactID = contactID; }

    /**
     * Sets the user ID.
     * @param userID The new user ID.
     */
    public void setUserID(int userID) { this.userID = userID; }

    /**
     * Sets the customer ID.
     * @param customerID The new customer ID.
     */
    public void setCustomerID(int customerID) { this.customerID = customerID; }

    /**
     * Sets the name of the user who last updated the appointment.
     * @param lastUpdatedBy The name of the user who last updated the appointment.
     */
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    /**
     * Sets the timestamp of the last update.
     * @param lastUpdate The new last update timestamp.
     */
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Sets the name of the user who created the appointment.
     * @param createdBy The name of the user who created the appointment.
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     * Sets the creation timestamp.
     * @param createDate The new creation timestamp.
     */
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    /**
     * Sets the end time of the appointment.
     * @param endTime The new end time.
     */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    /**
     * Sets the start time of the appointment.
     * @param startTime The new start time.
     */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    /**
     * Sets the meeting type.
     * @param meetingType The new meeting type.
     */
    public void setMeetingType(String meetingType) { this.meetingType = meetingType; }

    /**
     * Sets the location of the appointment.
     * @param location The new location.
     */
    public void setLocation(String location) { this.location = location; }

    /**
     * Sets the description of the appointment.
     * @param description The new description.
     */
    public void setDescription(String description) { this.description = description; }

    /**
     * Sets the title of the appointment.
     * @param title The new title.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Sets the appointment ID.
     * @param appointmentID The new appointment ID.
     */
    public void setAppointmentID(int appointmentID) { this.appointmentID = appointmentID; }

}