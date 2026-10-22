package services;

import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;

public class ClearService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public ClearService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public void clear() throws DataAccessException{
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }
}
