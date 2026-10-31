package dataaccess;
import java.sql.*;

public class DatabaseConnector {

    public static Connection getConnection() throws DataAccessException {
        return DatabaseManager.getConnection();
    }
}
