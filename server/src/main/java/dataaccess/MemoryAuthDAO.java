package dataaccess;

import model.AuthData;
import java.util.HashMap;

public class MemoryAuthDAO  implements AuthDAO{

    private final HashMap<String, AuthData> tokens = new HashMap<>();

    @Override
    public void createAuth(AuthData auth) throws DataAccessException {
        tokens.put(auth.authToken(), auth);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        tokens.remove(authToken);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return tokens.get(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        tokens.clear();
    }
}
