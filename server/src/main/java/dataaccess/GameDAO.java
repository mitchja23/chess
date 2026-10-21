package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String game) throws DataAccessException;
    GameData getGame(int id) throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    Collection<GameData> listGames()throws DataAccessException;
    void clear() throws  DataAccessException;
}
