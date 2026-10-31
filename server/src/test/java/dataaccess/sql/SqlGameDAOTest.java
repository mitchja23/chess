package dataaccess.sql;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;


import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SqlGameDAOTest {

    private GameDAO gameDAO;

    @BeforeEach
    public void setup() throws Exception{
        gameDAO = new SqlGameDAO();
        gameDAO.clear();
    }

    @Test
    @DisplayName("createGame")
    public void createGame() throws Exception {
        var data = gameDAO.createGame("game");
        Assertions.assertTrue(data > 0, "failed to create game");
        var result = gameDAO.getGame(data);

        Assertions.assertNotNull(result, "failed to create game");
        Assertions.assertEquals("game", result.gameName(), "names do not match");
        Assertions.assertNull(result.whiteUsername(), "White username it not empty");
        Assertions.assertNull(result.blackUsername(), "Black username it not empty");
        Assertions.assertNotNull(result.game(), "failed to create game");
    }

    @Test
    @DisplayName("createGameBad")
    public void createGameBad() throws Exception {
        Assertions.assertThrows(DataAccessException.class,
                () -> gameDAO.createGame(null),
                "created a empty name");
    }

    @Test
    @DisplayName("getGame")
    public void getGame() throws Exception {
        var data = gameDAO.createGame("game");
        var result = gameDAO.getGame(data);
        Assertions.assertNotNull(result, "failed to get game");

    }

    @Test
    @DisplayName("getGameBad")
    public void getGameBad() throws Exception {
        var result = gameDAO.getGame(123);
        Assertions.assertNull(result, "gave a game that does not exist");
    }

    @Test
    @DisplayName("updateGame")
    public void updateGame() throws Exception {
        var data = gameDAO.createGame("game");
        var game = new GameData(data,"jacob", "mitch", "game", new ChessGame());
        gameDAO.updateGame(game);

        var result = gameDAO.getGame(data);
        Assertions.assertEquals("jacob",result.whiteUsername(), "White username not updated");
        Assertions.assertEquals("mitch",result.blackUsername(), "Black username not updated");
    }

    @Test
    @DisplayName("updateGameBad")
    public void updateGameBad() throws Exception {
        var game = gameDAO.createGame("game");
        var data = new GameData(game,"jacob", "mitch", null, new ChessGame());
        Assertions.assertThrows(DataAccessException.class,
                () -> gameDAO.updateGame(data),
                "updated game");
    }

    @Test
    @DisplayName("listGame")
    public void listGame() throws Exception {
        gameDAO.createGame("game1");
        gameDAO.createGame("game2");


        var result = gameDAO.listGames();
        Assertions.assertEquals(2,result.size(), "listed incorrect amount of games");

    }

    @Test
    @DisplayName("listGameBad")
    public void listGameBad() throws Exception {
        var result = gameDAO.listGames();
        Assertions.assertNotNull(result, "listed null games");

    }

    @Test
    @DisplayName("clear")
    public void clear() throws Exception {
        var game1 = gameDAO.createGame("game1");
        var game2 = gameDAO.createGame("game2");
        gameDAO.clear();
        Assertions.assertNull(gameDAO.getGame(game1),"game was not cleared");
        Assertions.assertNull(gameDAO.getGame(game2),"game was not cleared");
    }









}
