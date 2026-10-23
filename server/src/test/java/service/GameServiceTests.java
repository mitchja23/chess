package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;

public class GameServiceTests {

    private GameDAO gameDAO;
    private GameService service;

    @BeforeEach
    public void setup() throws Exception {
        AuthDAO authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        service = new GameService(gameDAO, authDAO);
        authDAO.createAuth(new AuthData("123", "jacob"));
    }

    @Test
    @DisplayName("create game")
    public void createGameSuccess() throws Exception {
        var result = service.createGame("123", new Request.CreateGameRequest("game"));

        Assertions.assertNotNull(gameDAO.getGame(result.gameID()), "failed to make game");
    }

    @Test
    @DisplayName("unauthorized game")
    public void createGameUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.createGame("bad", new Request.CreateGameRequest("game")),
                "failed to make game");
    }

    @Test
    @DisplayName("game list")
    public void listGamesSuccess() throws Exception {
        service.createGame("123", new Request.CreateGameRequest("game"));
        var result = service.listGames("123");
        Assertions.assertEquals(1, result.games().size(), "failed to list game");
    }

    @Test
    @DisplayName("unauthorized game list")
    public void listGamesUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class, () -> service.listGames("bad"),
                "failed to list game");
    }

    @Test
    @DisplayName("join game")
    public void joinGameSuccess() throws Exception {
        int gameID = service.createGame("123", new Request.CreateGameRequest("game")).gameID();
        service.joinGame("123", new Request.JoinGameRequest("WHITE", gameID));
        Assertions.assertEquals("jacob", gameDAO.getGame(gameID).whiteUsername(),
                "failed to join game");
    }

    @Test
    @DisplayName("join game not found")
    public void joinGameNotFound() {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.joinGame("123", new Request.JoinGameRequest("WHITE", 999)),
                "joined a game that does not exist");
    }

    @Test
    @DisplayName("color taken")
    public void joinGameAlreadyTaken() throws Exception {
        int gameID = service.createGame("123", new Request.CreateGameRequest("game")).gameID();
        service.joinGame("123", new Request.JoinGameRequest("WHITE", gameID));

        Assertions.assertThrows(AlreadyTakenException.class,
                () -> service.joinGame("123", new Request.JoinGameRequest("WHITE", gameID)),
                "color taken");
    }
}
