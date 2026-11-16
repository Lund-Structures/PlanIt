package Util.DAO;

import Util.DataQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** CountryDAO Implementation class of CountryDAO interface.
 * This class allows for the retrieval of all country records from the database.
 * */
public class ContactDAOImp implements ContactDAO {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private ObservableList<Contact> contacts = FXCollections.observableArrayList();
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructor method to instantiate ContactDAOImpl.
     * @param  incomingConn sets the connection
     */
    public ContactDAOImp(Connection incomingConn) {
        this.conn = incomingConn;
    }


    //--------------------------------------Contact Retrieval Methods------------------------------------------//
    /** {@inheritDoc} */
    @Override
    public ObservableList<Contact> getAllContacts() {
        //Method instant variables
        int contactID;
        String contactName;
        String email;

        //Run SQL query and add result set to contact list
        try {
            DataQuery.setPreparedStatement(conn, "SELECT * FROM contacts");
            ps = DataQuery.getPreparedStatement();
            rs = ps.executeQuery();
            while(rs.next()) {
                contactID = rs.getInt("Contact_ID");
                contactName = rs.getString("Contact_Name");
                email = rs.getString("Email");

                Contact contact = new Contact(contactID, contactName, email);
                contacts.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //Return contact list
        return contacts;
    }

    /** {@inheritDoc} */
    @Override
    public Contact getContact(int contactID) throws SQLException {
        //Initialize method variables
        int ID = 0;
        String contactName = "";
        String email = "";

        //Run SQL query and find matching contact ID's
        try {
            DataQuery.setPreparedStatement(conn, "SELECT * FROM contacts");
            ps = DataQuery.getPreparedStatement();
            rs = ps.executeQuery();
            while (rs.next()) {
                if (contactID == rs.getInt("Contact_ID")){ //Contact found if True
                    ID = rs.getInt("Contact_ID");
                    contactName = rs.getString("Contact_Name");
                    email = rs.getString("Email");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Contact(ID, contactName, email);
    }
}
