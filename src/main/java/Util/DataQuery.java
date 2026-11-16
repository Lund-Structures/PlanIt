package Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataQuery {
    // statement reference object
    private static PreparedStatement statement;

    /**
     * setPreparedStatement. Set a prepared statement from connection to the database.
     * @param conn Connection object
     * @param sqlStatement SQL statement used to query the database
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public static void setPreparedStatement(Connection conn, String sqlStatement) throws SQLException {
        assert conn != null;
        statement = conn.prepareStatement(sqlStatement);
    }

    /**
     * getPreparedStatement. Grab the statement for use to query the database.
     * @return PreparedStatement statement
     */
    public static PreparedStatement getPreparedStatement() {
        return statement;
    }

}
