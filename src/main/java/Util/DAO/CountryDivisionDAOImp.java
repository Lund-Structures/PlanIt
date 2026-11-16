package Util.DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.CountryDivision;
import java.sql.*;

/**
 * The CountryDivisionDAOImp class implements the CountryDivisionDAO interface.
 * It provides methods to retrieve country division records from the database.
 */
public class CountryDivisionDAOImp implements CountryDivisionDAO{

    //--------------------------------------------Instant Variable---------------------------------------------//
    private Connection conn;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a CountryDivisionDAOImp object with a given database connection.
     *
     * @param incomingConn The database connection to be used for queries.
     */
    public CountryDivisionDAOImp(Connection incomingConn) {
        this.conn = incomingConn;
    }


    //--------------------------------------Retrieve Country Division Methods-----------------------------------//
    /** {@inheritDoc} */
    @Override
    public ObservableList<CountryDivision> getAllDivisions() {

        //SQL statement
        String selectAllDivisions = "SELECT * FROM first_level_divisions";
        //Initialize allDivisions list
        ObservableList<CountryDivision> allDivisions = FXCollections.observableArrayList();

        //Run SQL and add result set to allDivisions list
        try (PreparedStatement ps = conn.prepareStatement(selectAllDivisions)){
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int countryID = rs.getInt("Country_ID");
                int divID = rs.getInt("Division_ID");
                String divName = rs.getString("Division");

                CountryDivision firstDivision = new CountryDivision(countryID, divID, divName);
                allDivisions.add(firstDivision);
            }
        } catch(SQLException e) {
            System.out.println("There was an error: " + e.getMessage());
            e.printStackTrace();
        }
        return allDivisions;
    }

    /** {@inheritDoc} */
    @Override
    public ObservableList<CountryDivision> getDivision (int countryID) throws SQLException {
        //SQL statement
        String selectDivision = "SELECT * FROM " +
                "first_level_divisions " +
                "WHERE " +
                "Country_ID = ?";
        //Initialize firstDivision list
        ObservableList<CountryDivision> firstDivision = FXCollections.observableArrayList();

        //Run Query and add Result Set to firstDivision list
        try (PreparedStatement ps = conn.prepareStatement(selectDivision)) {
            ps.setInt(1, countryID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int divNumber = rs.getInt("Division_ID");
                String divName = rs.getString("Division");
                int countryNumber = rs.getInt("Country_ID");

                CountryDivision division = new CountryDivision(countryNumber, divNumber, divName);
                firstDivision.add(division);
            }
        }
        //return firstDivision list
        return firstDivision;
    }

    /** {@inheritDoc} */
    @Override
    public Country getCountry(int divisionID) {
        //SQL statment
        String selectCountry = "SELECT * FROM " +
                                    "first_level_divisions " +
                                "WHERE " +
                                    "Division_ID = ?";

        //Run SQL and return country with matching divisionID
        try (PreparedStatement ps = conn.prepareStatement(selectCountry)) {
            ps.setInt(1, divisionID);
            ResultSet rs = ps.executeQuery();

            rs.next();
            int countryID = rs.getInt("Country_ID");

            PreparedStatement countryStatement = conn.prepareStatement("SELECT * FROM countries WHERE Country_ID = ?");
            countryStatement.setInt(1, countryID);
            rs = countryStatement.executeQuery();
            rs.next();

            return new Country(countryID, rs.getString("Country"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    }
