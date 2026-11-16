package Util.DAO;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Country;
import model.CountryDivision;
import java.sql.*;

/**
 * The CountryDivisionDAO interface provides method definitions
 * for retrieving country division records from the database.
 */
public interface CountryDivisionDAO {

    /**
     * getAllDivisions. Return an observable list of all divisions in the database.
     * @return ObservableList<FirstLevelDivision> of all CountryDivision objects.
     */
    public ObservableList<CountryDivision> getAllDivisions() throws SQLException;

    /**
     * Retrieves all divisions associated with a specific country ID.
     *
     * @param countryID The ID of the country for which to fetch divisions.
     * @return An ObservableList of CountryDivision objects that belong to the specified country.
     * @throws SQLException If a database access error occurs.
     */
    public ObservableList<CountryDivision> getDivision(int countryID) throws SQLException;

    /**
     * Retrieves country associated with a specific division ID.
     *
     * @param divisionID The ID of the country for which to fetch divisions.
     * @return An ObservableList of CountryDivision objects that belong to the specified country.
     * @throws SQLException If a database access error occurs.
     */
    Country getCountry(int divisionID) throws SQLException;
}
