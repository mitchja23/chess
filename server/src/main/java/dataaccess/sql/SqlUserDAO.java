package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class SqlUserDAO implements UserDAO {

    public SqlUserDAO() throws DataAccessException{
        DatabaseManager.createDatabase();
        var createTable = """
        CREATE TABLE IF NOT EXISTS user (
            username VARCHAR(255) NOT NULL,
            password VARCHAR(255) NOT NULL,
            email VARCHAR(255) NOT NULL,
            PRIMARY KEY (username)
        )""";
        try (var connect = DatabaseManager.getConnection();
             var newTable = connect.prepareStatement(createTable)) {
            newTable.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not make table", ex);
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        var createUser = """
                INSERT INTO user (username, password, email)
                VALUES (?,?,?)
                """;
        var hashedPass = BCrypt.hashpw(user.password(), BCrypt.gensalt());



        try (var connect = DatabaseManager.getConnection();
             var newUser = connect.prepareStatement(createUser)) {
            newUser.setString(1,user.username());
            newUser.setString(2,hashedPass);
            newUser.setString(3,user.email());
            newUser.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not make user", ex);
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        var getUser = """
                SELECT username, password, email
                FROM user
                WHERE username = ?
                """;
        try (var connect = DatabaseManager.getConnection();
             var currentUser = connect.prepareStatement(getUser)) {
            currentUser.setString(1, username);
            try (var queryResult = currentUser.executeQuery()) {
                if (queryResult.next()) {
                    return new UserData(queryResult.getString("username"), queryResult.getString("password"), queryResult.getString("email"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("could not get user", ex);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var clearUser = """
                DELETE FROM user
                """;
        try (var connect = DatabaseManager.getConnection();
             var deleteUser = connect.prepareStatement(clearUser)) {
            deleteUser.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not clear user", ex);
        }

    }
}
