package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseConnector;
import dataaccess.DatabaseManager;
import model.AuthData;


import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO {

    public SqlAuthDAO() throws DataAccessException{
        var createTable = """
        CREATE TABLE IF NOT EXISTS auth(
            authToken VARCHAR(255) NOT NULL,
            username VARCHAR(255) NOT NULL,
            PRIMARY KEY (authToken)
                        )""";
        DatabaseConnector.createTable(createTable);
    }

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        var createAuth = """
                INSERT INTO auth (authToken, username)
                VALUES (?,?)
                """;

        try (var connect = DatabaseConnector.getConnection();
             var newAuth = connect.prepareStatement(createAuth)) {
            newAuth.setString(1,auth.authToken());
            newAuth.setString(2,auth.username());
            newAuth.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not create authToken", ex);
        }

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        var getAuth = """
                SELECT authToken, username
                FROM auth
                WHERE authToken = ?
                """;
        try (var connect = DatabaseConnector.getConnection();
             var currentAuth = connect.prepareStatement(getAuth)) {
            currentAuth.setString(1, authToken);
            try (var queryResult = currentAuth.executeQuery()) {
                if (queryResult.next()) {
                    return new AuthData(queryResult.getString("authToken"), queryResult.getString("username"));
                } else {
                    return null;
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("could not get auth", ex);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        var deleteAuth = """
                DELETE FROM auth
                WHERE authToken = ?
                """;
        try (var connect = DatabaseConnector.getConnection();
             var delAuth = connect.prepareStatement(deleteAuth)) {
            delAuth.setString(1,authToken);
            delAuth.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not delete auth token", ex);
        }

    }

    @Override
    public void clear() throws DataAccessException {
        var clearAuth = """
                DELETE FROM auth
                """;
        try (var connect = DatabaseConnector.getConnection();
             var wipeAuth = connect.prepareStatement(clearAuth)) {
            wipeAuth.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not clear auth token", ex);
        }

    }

}
