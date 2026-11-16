package model;

/**
 * Represents a first-level division, which contains details about the division's unique ID, associated country, and name.
 */
public class FirstLvDivision {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private int divisionID;
    private int countryID;
    private String division;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a FirstLvDivision object with the specified division ID, country ID, and division name.
     *
     * @param divisionID the unique ID of the division
     * @param countryID the ID of the country the division belongs to
     * @param division the name of the division
     */
    public FirstLvDivision(int divisionID, int countryID, String division) {
        this.divisionID = divisionID;
        this.countryID = countryID;
        this.division = division;
    }


    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Returns the unique ID of the division.
     * @return the division ID
     */
    public int getDivisionID() { return divisionID; }

    /**
     * Returns the ID of the country the division belongs to.
     * @return the country ID
     */
    public int getCountryID() { return countryID; }

    /**
     * Returns the name of the division.
     * @return the division name
     */
    public String getDivision() { return division; }


    //-----------------------------------------------Set Methods-----------------------------------------------//
    // This section is intentionally left blank
}