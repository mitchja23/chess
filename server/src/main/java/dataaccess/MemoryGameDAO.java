package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{

    private final HashMap<Integer, GameData> games = new HashMap<>();
    private int nextGame = 1;


    @Override
    public int createGame(String game) throws DataAccessException {
        int id = nextGame++;
        games.put(id, new GameData(id, null, null, game, new ChessGame()));
        return id;
    }

    @Override
    public GameData getGame(int id) throws DataAccessException {
        return games.get(id);
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
