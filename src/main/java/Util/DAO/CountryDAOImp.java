package Util.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;

import java.sql.*;

/**
 * Implementation class of the CountryDAO interface.
 * This class provides methods to retrieve all country records from the database.
 */
public class CountryDAOImp implements CountryDAO {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private ObservableList<Country> countries = FXCollections.observableArrayList();
    private Connection conn;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a CountryDAOImp object with a specified database connection.
     *
     * @param incomingConn The database connection to be used.
     * @throws SQLException If an error occurs while establishing the connection.
     */
        public CountryDAOImp(Connection incomingConn) throws SQLException {
            this.conn = incomingConn;
        }


    //-----------------------------------------------Get Countries Method-------------------------------------//
    /** {@inheritDoc} */
    @Override
    public ObservableList<Country> getAllCountries() {
        //SQL statement
        String allCountries = "SELECT * FROM countries";

        //Run SQL query and add result set to countries list
        try (PreparedStatement ps = conn.prepareStatement(allCountries)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {//Can we take away more parameters??
                int countryID = rs.getInt("Country_ID");
                String countryName = rs.getString("Country");
                Timestamp date = rs.getTimestamp("Create_Date");
                String createdBy = rs.getString("Created_By");
                Country country = new Country(countryID, countryName);
                countries.add(country);
            }
        } catch(SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        //Return countries list
        return countries;
    }


    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Set methods for this class are intentionally left blank.
     * This section may be populated in the future if needed.
     */

}
