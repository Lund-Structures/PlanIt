package model;

import java.time.LocalDateTime;

/**
 * Represents a customer with personal details, contact information, and timestamps for creation and last update.
 * This class provides methods to access and modify the customer's information.
 */
public class Customer {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private int customerID;
    private String customerName;
    private String address;
    private String zip;
    private String phone;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdatedBy;
    private int divisionID;

    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a Customer object with the specified details.
     *
     * @param customerID the unique ID of the customer
     * @param customerName the name of the customer
     * @param address the address of the customer
     * @param zip the zip code of the customer
     * @param phone the phone number of the customer
     * @param createDate the date and time when the customer was created
     * @param createdBy the user who created the customer record
     * @param lastUpdate the date and time when the customer record was last updated
     * @param lastUpdatedBy the user who last updated the customer record
     * @param divisionID the division ID the customer belongs to
     */
    public Customer(int customerID, String customerName, String address, String zip, String phone, LocalDateTime createDate,
                    String createdBy, LocalDateTime lastUpdate, String lastUpdatedBy, int divisionID) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.address = address;
        this.zip = zip;
        this.phone = phone;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.divisionID = divisionID;
    }

    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Returns the unique ID of the customer.
     * @return the customer ID
     */
    public int getCustomerID() { return customerID; }

    /**
     * Returns the name of the customer.
     * @return the customer name
     */
    public String getCustomerName() { return customerName; }

    /**
     * Returns the address of the customer.
     * @return the customer address
     */
    public String getAddress() { return address; }

    /**
     * Returns the zip code of the customer.
     * @return the customer zip code
     */
    public String getZip() { return zip; }

    /**
     * Returns the phone number of the customer.
     * @return the customer phone number
     */
    public String getPhone() { return phone; }

    /**
     * Returns the date and time when the customer was created.
     * @return the customer creation date
     */
    public LocalDateTime getCreateDate() { return createDate; }

    /**
     * Returns the user who created the customer record.
     * @return the user who created the customer record
     */
    public String getCreatedBy() { return createdBy; }

    /**
     * Returns the date and time when the customer record was last updated.
     * @return the last update date of the customer record
     */
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    /**
     * Returns the user who last updated the customer record.
     * @return the user who last updated the customer record
     */
    public String getLastUpdatedBy() { return lastUpdatedBy; }

    /**
     * Returns the division ID the customer belongs to.
     * @return the division ID
     */
    public int getDivisionID() { return divisionID; }


    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Sets the unique ID of the customer.
     * @param customerID the unique ID to set for the customer
     */
    public void setCustomerID(int customerID) { this.customerID = customerID; }

    /**
     * Sets the name of the customer.
     * @param customerName the name to set for the customer
     */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /**
     * Sets the address of the customer.
     * @param address the address to set for the customer
     */
    public void setAddress(String address) { this.address = address; }

    /**
     * Sets the zip code of the customer.
     * @param zip the zip code to set for the customer
     */
    public void setZip(String zip) { this.zip = zip; }

    /**
     * Sets the phone number of the customer.
     * @param phone the phone number to set for the customer
     */
    public void setPhone(String phone) { this.phone = phone; }

    /**
     * Sets the date and time when the customer was created.
     * @param createDate the creation date to set for the customer
     */
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    /**
     * Sets the user who created the customer record.
     * @param createdBy the user to set as the creator of the customer record
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     * Sets the date and time when the customer record was last updated.
     * @param lastUpdate the last update date to set for the customer record
     */
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Sets the user who last updated the customer record.
     * @param lastUpdatedBy the user to set as the last updater of the customer record
     */
    public void setLastUpdatedBy(String lastUpdatedBy) { this.lastUpdatedBy = lastUpdatedBy; }

    /**
     * Sets the division ID the customer belongs to.
     * @param divisionID the division ID to set for the customer
     */
    public void setDivisionID(int divisionID) { this.divisionID = divisionID; }

}