package model;

import java.time.LocalDateTime;

/**
 * Represents a user with details including user ID, username, password,
 * creation date, who created the account, last update,
 * and the user who last updated the account.
 */
public class User {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private int userID;
    private String userName;
    private String password;
    private LocalDateTime createDate;
    private String createdBy;
    private LocalDateTime lastUpdate;
    private String lastUpdateBy;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a new User with the specified details.
     *
     * @param userID      the user's unique ID
     * @param userName    the user's username
     * @param password    the user's password
     * @param createDate  the date the user account was created
     * @param createdBy   the username of the user who created the account
     * @param lastUpdate  the date the user account was last updated
     * @param lastUpdateBy the username of the user who last updated the account
     */
    public User(int userID, String userName, String password, LocalDateTime createDate, String createdBy, LocalDateTime lastUpdate, String lastUpdateBy) {
        this.userID = userID;
        this.password = password;
        this.userName = userName;
        this.createDate = createDate;
        this.createdBy = createdBy;
        this.lastUpdate = lastUpdate;
        this.lastUpdateBy = lastUpdateBy;
    }


    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Returns the unique ID of the user.
     * @return the userID
     */
    public int getUserID() { return userID; }

    /**
     * Returns the username of the user.
     * @return the userName
     */
    public String getUserName() { return userName; }

    /**
     * Returns the password of the user.
     * @return the password
     */
    public String getPassword() { return password; }

    /**
     * Returns the creation date of the user account.
     * @return the createDate
     */
    public LocalDateTime getCreateDate() { return createDate; }

    /**
     * Returns the username of the user who created the account.
     * @return the createdBy
     */
    public String getCreatedBy() { return createdBy; }

    /**
     * Returns the last update date of the user account.
     * @return the lastUpdate
     */
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    /**
     * Returns the username of the user who last updated the account.
     * @return the lastUpdateBy
     */
    public String getLastUpdateBy() { return lastUpdateBy; }


    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Sets the unique ID of the user.
     * @param userID the userID to set
     */
    public void setUserID(int userID) { this.userID = userID; }

    /**
     * Sets the username of the user.
     * @param userName the userName to set
     */
    public void setUserName(String userName) { this.userName = userName; }

    /**
     * Sets the password of the user.
     * @param password the password to set
     */
    public void setPassword(String password) { this.password = password; }

    /**
     * Sets the creation date of the user account.
     * @param createDate the createDate to set
     */
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    /**
     * Sets the username of the user who created the account.
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     * Sets the last update date of the user account.
     * @param lastUpdate the lastUpdate to set
     */
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }

    /**
     * Sets the username of the user who last updated the account.
     * @param lastUpdateBy the lastUpdateBy to set
     */
    public void setLastUpdateBy(String lastUpdateBy) { this.lastUpdateBy = lastUpdateBy; }

}