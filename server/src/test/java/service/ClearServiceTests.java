package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class ClearServiceTests {

    @Test
    @DisplayName("Clear")
    public void clearSuccess() throws Exception {
        UserDAO userDAO = new MemoryUserDAO();
        AuthDAO authDAO = new MemoryAuthDAO();
        GameDAO gameDAO = new MemoryGameDAO();

        userDAO.createUser(new UserData("jacob", "password", "jacob@mail.com"));
        authDAO.createAuth(new AuthData("123", "jacob"));
        int gameID = gameDAO.createGame("testGame");

        new ClearService(gameDAO, authDAO, userDAO).clear();

        Assertions.assertNull(userDAO.getUser("jacob"), "not cleared");
        Assertions.assertNull(authDAO.getAuth("123"), "not cleared");
        Assertions.assertNull(gameDAO.getGame(gameID), "not cleared");
    }
}
