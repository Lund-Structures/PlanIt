package model;

/**
 * Represents a country with a unique ID and name.
 * This class provides methods to access the country ID and name.
 */
public class Country {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private int countryID;
    private String country;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a Country object with the specified country ID and name.
     *
     * @param countryID the unique ID of the country
     * @param country the name of the country
     */
    public Country(int countryID, String country) {
        this.countryID = countryID;
        this.country = country;
    }


    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Returns the unique ID of the country.
     * @return the country ID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Returns the name of the country.
     * @return the name of the country
     */
    public String getCountry() {
        return country;
    }


    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Set methods for this class are intentionally left blank.
     * This section may be populated in the future if needed.
     */
}