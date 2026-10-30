package service;

import dataaccess.AuthDAO;
import model.AuthData;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public Results.AuthResult register(Request.RegisterRequest register) throws AlreadyTakenException, BadRequestException, DataAccessException {
       if(register.username() == null || register.password() == null || register.email() == null){
           throw new BadRequestException("Missing parameters");
       }
       if(userDAO.getUser(register.username()) != null){
           throw new AlreadyTakenException("Username already taken");
       }
       userDAO.createUser(new UserData(register.username(), register.password(), register.email()));

        String token = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(token, register.username()));

       return new Results.AuthResult(register.username(), token);
    }

    public Results.AuthResult login(Request.LoginRequest login) throws BadRequestException, DataAccessException, UnauthorizedException{
        if(login.username() == null || login.password() == null){
            throw new BadRequestException("Enter Username and Password");
        }
        UserData user = userDAO.getUser(login.username());
        if(user == null || !BCrypt.checkpw(login.password(), user.password())){
            throw new UnauthorizedException("Username or Password incorrect");
        }
        String token = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(token, login.username()));

        return new Results.AuthResult(login.username(), token);
    }
    public void logout(String authToken) throws DataAccessException, UnauthorizedException{
        if(authDAO.getAuth(authToken) == null){
            throw  new UnauthorizedException("Logout Request Failed");
        }
        authDAO.deleteAuth(authToken);
    }
}
