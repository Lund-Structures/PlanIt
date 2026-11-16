package model;

/**
 * Represents a report entry with details for a specific month, type, and total.
 */
public class Report1 {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private final String month;
    private final String type;
    private final Integer total;


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructs a Report1 object with the specified month, type, and total.
     *
     * @param month the month for the report
     * @param type the type of report (e.g., sales, transactions, etc.)
     * @param total the total value associated with the report
     */
    public Report1(String month, String type, Integer total) {
        this.month = month;
        this.type = type;
        this.total = total;
    }

    //-----------------------------------------------Get Methods-----------------------------------------------//
    /**
     * Returns the month of the report.
     * @return the month
     */
    public String getMonth() {return month;}

    /**
     * Returns the type of the report.
     * @return the type of the report
     */
    public String getType() {return type;}

    /**
     * Returns the total value of the report.
     * @return the total value
     */
    public Integer getTotal() {return total;}

    //-----------------------------------------------Set Methods-----------------------------------------------//
    /**
     * Set methods for this class are intentionally left blank.
     * This section may be populated in the future if needed.
     */
}