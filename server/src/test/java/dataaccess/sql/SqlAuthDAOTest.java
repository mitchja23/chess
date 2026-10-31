package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SqlAuthDAOTest {

    private AuthDAO authDAO;

    @BeforeEach
    public void setup() throws Exception{
        authDAO = new SqlAuthDAO();
        authDAO.clear();
    }

    @Test
    @DisplayName("createAuth")
    public void createAuth() throws Exception {
        var data = new AuthData("123", "jacob");
        authDAO.createAuth(data);

        var result = authDAO.getAuth("123");
        Assertions.assertNotNull(result, "failed to create auth");
        Assertions.assertEquals(data.authToken(), result.authToken(), "wrong token");
        Assertions.assertEquals(data.username(), result.username(), "wrong username");
    }

    @Test
    @DisplayName("createAuthBad")
    public void createAuthBad() throws Exception {
        var token1 = new AuthData("123", "jacob1");
        var token2 = new AuthData("123", "jacob2");
        authDAO.createAuth(token1);
        Assertions.assertThrows(DataAccessException.class,
                () -> authDAO.createAuth(token2),
                "created a duplicate auth token");
    }

    @Test
    @DisplayName("getAuth")
    public void getAuth() throws Exception {
        var data = new AuthData("123", "jacob");
        authDAO.createAuth(data);

        var result = authDAO.getAuth("123");
        Assertions.assertNotNull(result, "failed to get auth");
        Assertions.assertEquals(data.authToken(), result.authToken(), "wrong token");
        Assertions.assertEquals(data.username(), result.username(), "wrong username");
    }

    @Test
    @DisplayName("getAuthBad")
    public void getAuthBad() throws Exception {
        var result = authDAO.getAuth("123");
        Assertions.assertNull(result, "gave a token that does not exist");
    }

    @Test
    @DisplayName("deleteAuth")
    public void deleteAuth() throws Exception {
        var data = new AuthData("123", "jacob");
        authDAO.createAuth(data);
        authDAO.deleteAuth("123");

        var result = authDAO.getAuth("123");
        Assertions.assertNull(result, "token was not deleted");
    }

    @Test
    @DisplayName("deleteAuthBad")
    public void deleteAuthBad() throws Exception {
        var token1 = new AuthData("123", "jacob");
        var token2 = new AuthData("234", "jacob2");
        authDAO.createAuth(token1);
        authDAO.createAuth(token2);
        authDAO.deleteAuth("234");

        var result = authDAO.getAuth("123");
        Assertions.assertNotNull(result, "deleted incorrect token");
    }


    @Test
    @DisplayName("clear")
    public void clear() throws Exception {
        var token1 = new AuthData("123", "jacob1");
        var token2 = new AuthData("234", "jacob2");
        authDAO.createAuth(token1);
        authDAO.createAuth(token2);
        authDAO.clear();
        Assertions.assertNull(authDAO.getAuth("123"),"token was not cleared");
        Assertions.assertNull(authDAO.getAuth("234"),"token was not cleared");
    }




}
