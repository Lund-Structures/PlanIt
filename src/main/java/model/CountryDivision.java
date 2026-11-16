package model;

/**
 * Represents a division within a specific country.
 * This class provides methods to access the country ID, division ID, and division name.
 */
public class CountryDivision {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private int countryID;
    private int divisionID;
    private String name;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a CountryDivision object with the specified country ID, division ID, and division name.
     *
     * @param countryNumber the unique ID of the country
     * @param divNumber the unique ID of the division
     * @param divName the name of the division
     */
    public CountryDivision(int countryNumber, int divNumber, String divName) {
        this.countryID = countryNumber;
        this.divisionID = divNumber;
        this.name = divName;
    }


    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Returns the unique ID of the country associated with this division.
     * @return the country ID
     */
    public int getCountryID() {return this.countryID;}

    /**
     * Returns the unique ID of the division.
     * @return the division ID
     */
    public int getDivisionID() {return this.divisionID;}

    /**
     * Returns the name of the division.
     * @return the name of the division
     */
    public String getName() {return this.name;}


    //-----------------------------------------------Set Methods-----------------------------------------------//
    // This section is intentionally left blank

}