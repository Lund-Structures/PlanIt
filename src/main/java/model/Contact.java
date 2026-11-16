package model;

/**
 * Represents a contact in the scheduling system.
 * This class holds information about a contact, including their ID, name, and email.
 */
public class Contact {

    //--------------------------------------------Instant Variables--------------------------------------------//
    /** Unique identifier for the contact. */
    private int contactID;

    /** Name of the contact. */
    private String name;

    /** Email address of the contact. */
    private String email;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a Contact object with the specified details.
     *
     * @param id The unique ID of the contact.
     * @param name The name of the contact.
     * @param email The email address of the contact.
     */
    public Contact(int id, String name, String email) {
        this.contactID = id;
        this.name = name;
        this.email = email;
    }


    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Gets the contact's unique ID.
     * @return The contact ID.
     */
    public int getContactID() { return contactID; }

    /**
     * Gets the contact's name.
     * @return The name of the contact.
     */
    public String getName() { return name; }

    /**
     * Gets the contact's email address.
     * @return The email address of the contact.
     */
    public String getEmail() { return email; }


    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Sets the contact's unique ID.
     * @param contactID The new contact ID.
     */
    public void setContactID(int contactID) { this.contactID = contactID; }

    /**
     * Sets the contact's name.
     * @param name The new name of the contact.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the contact's email address.
     * @param email The new email address.
     */
    public void setEmail(String email) { this.email = email; }

}