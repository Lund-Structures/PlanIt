package Util.DAO;

import Util.DataQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * The UserDAOImp class provides an implementation of the UserDAO interface.
 * It allows interaction with the database to retrieve, create, and update User records.
 */
public class UserDAOImp implements UserDAO {

    //--------------------------------------------Instant Variables--------------------------------------------//
    private Connection conn;
    private PreparedStatement ps;
    private ResultSet rs;
    private ObservableList<User> userList = FXCollections.observableArrayList();


    //-----------------------------------------------Constructor-----------------------------------------------//
    /**
     * Constructor to initialize the database connection.
     *
     * @param incomingConn The database connection to be used by the DAO.
     */
    public UserDAOImp(Connection incomingConn) {
        this.conn = incomingConn;
    }


    //---------------------------------------------Get User Methods--------------------------------------------//
    /** {@inheritDoc} */
    @Override
    public ObservableList<User> getAllUsers() {
        try {
            DataQuery.setPreparedStatement(conn, "SELECT * FROM users");
            ps = DataQuery.getPreparedStatement();
            rs = ps.executeQuery();

            while (rs.next()) {
                int userID = rs.getInt("User_ID");
                String userName = rs.getString("User_Name");
                String password = rs.getString("Password");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");

                User user = new User(userID, userName, password, createDate, createdBy, lastUpdate, lastUpdateBy);
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving users: " + e.getMessage(), e);
        }
        return userList;
    }

    /** {@inheritDoc} */
    @Override
    public User getUser(int userID) throws SQLException {
        //SQL Statement
        String sql = "SELECT * FROM users WHERE User_ID = ?";

        //Run SQL statement and save result to result set
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("User_ID"),
                        rs.getString("User_Name"),
                        rs.getString("Password"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
                        rs.getString("Last_Updated_By")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error getting user by userID: " + e.getMessage());
        }
        //Return null if no user exists
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public User getUser(String userName) throws SQLException {
        try {
            DataQuery.setPreparedStatement(conn, "SELECT * FROM users WHERE User_Name = ?");
            ps = DataQuery.getPreparedStatement();
            ps.setString(1, userName);
            rs = ps.executeQuery();

            if (rs.next()) {
                int userID = rs.getInt("User_ID");
                String name = rs.getString("User_Name");
                String password = rs.getString("Password");
                LocalDateTime createDate = rs.getTimestamp("Create_Date").toLocalDateTime();
                String createdBy = rs.getString("Created_By");
                LocalDateTime lastUpdate = rs.getTimestamp("Last_Update").toLocalDateTime();
                String lastUpdateBy = rs.getString("Last_Updated_By");

                return new User(userID, name, password, createDate, createdBy, lastUpdate, lastUpdateBy);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving user by username: " + e.getMessage(), e);
        }
        return null;
    }


    //-------------------------------------------Set User Methods----------------------------------------------//
    /**
     * Retrieves a user based on the provided username and password.
     * This method is typically used for login functionality.
     *
     * @param name The username of the user.
     * @param code The password of the user.
     * @return A User object if a matching user is found.
     * @throws SQLException if a database access error occurs.
     */
    public User setCurrentUser(String name, String code) throws SQLException {
        //SQL Statement
        String sql = "SELECT * FROM users WHERE BINARY User_Name = ? AND Password = ?";

        //Run SQL statement and save result to result set
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {//True if user exists
                return new User(
                        rs.getInt("User_ID"),
                        rs.getString("User_Name"),
                        rs.getString("Password"),
                        rs.getTimestamp("Create_Date").toLocalDateTime(),
                        rs.getString("Created_By"),
                        rs.getTimestamp("Last_Update").toLocalDateTime(),
                        rs.getString("Last_Updated_By")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error setting the user: " + e.getMessage(), e);
        }
        //If user exists return user, else return null

        return null;
    }
}