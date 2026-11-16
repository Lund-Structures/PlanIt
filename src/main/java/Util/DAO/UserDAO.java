package Util.DAO;

import javafx.collections.ObservableList;
import model.User;
import java.sql.SQLException;

/**
 * The UserDAO interface provides method definitions
 * for retrieving user records from the database.
 */
public interface UserDAO {

    /**
     * getAllUsers(). Method to return all users from the database.
     * @return ObservableList<User>
     */
    public ObservableList<User> getAllUsers();

    /**
     * getUser. Returns a specific user from the database.
     * @param userID Integer
     * @return User
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public User getUser(int userID) throws SQLException;

    /**
     * getUser. Overloaded get user that returns a specific user from the database.
     * @param userName String
     * @return User
     * @throws SQLException throws SQLException in case a database error occurs.
     */
    public User getUser(String userName) throws SQLException;

}
