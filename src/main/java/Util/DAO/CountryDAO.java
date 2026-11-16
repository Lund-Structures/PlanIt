package Util.DAO;


import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import model.Country;

/**
 * The countryDAO interface provides method definitions
 * for retrieving country records from the database.
 */
public interface CountryDAO {
    /**
     * getAllCountries. Returns an ObservableList of all countries.
     * @return ObservableList<Country>
     */
    public ObservableList<Country> getAllCountries();
}
