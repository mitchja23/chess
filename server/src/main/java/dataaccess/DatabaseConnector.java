package dataaccess;
import java.sql.*;

public class DatabaseConnector {

    public static Connection getConnection() throws DataAccessException {
        return DatabaseManager.getConnection();
    }

    public static void createTable(String createTable) throws  DataAccessException{
        DatabaseManager.createDatabase();
        try (var connect = DatabaseConnector.getConnection();
             var newTable = connect.prepareStatement(createTable)) {
            newTable.executeUpdate();
        } catch (SQLException ex) {
            throw new DataAccessException("could not make table", ex);
        }
    }
}
