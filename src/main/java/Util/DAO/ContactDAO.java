package Util.DAO;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Contact;

import java.sql.SQLException;

/**
 * The contactDAO interface provides method definitions
 * for retrieving contact records from the database.
 */
public interface ContactDAO {
    /**
     * getAllContacts. Return an observable list of all contacts from the database.
     * @return ObservableList<Contact>
     */
    public ObservableList<Contact> getAllContacts() ;

    /**
     * getContact. Returns specific contact.
     * @param contactId Integer
     * @return Contact
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public Contact getContact(int contactId) throws SQLException;
}
