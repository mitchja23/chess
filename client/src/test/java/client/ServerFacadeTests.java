package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        facade = new ServerFacade(port);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear() throws ResponseErrors{
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    @DisplayName("registerGood")
    public void registerGood() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");

        Assertions.assertNotNull(data, "failed to register");
        Assertions.assertEquals("jacob", data.username(), "usernames dont match");
        Assertions.assertNotNull(data.authToken(), "no auth token");

    }

    @Test
    @DisplayName("registerBad")
    public void registerBad() throws ResponseErrors{
        facade.register("jacob", "pass", "jacob@gmail.com");
        Assertions.assertThrows(ResponseErrors.class, () -> facade.register("jacob", "pass", "jacob2@gmail.com"),
                    "Trying to register with a taken name");
    }

    @Test
    @DisplayName("loginGood")
    public void loginGood() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");
        var login = facade.login("jacob", "pass");

        Assertions.assertNotNull(login, "failed to login");
        Assertions.assertEquals(login.username(), data.username(), "usernames dont match");
        Assertions.assertNotNull(login.authToken(), "no auth token");

    }

    @Test
    @DisplayName("loginBad")
    public void loginBad() throws ResponseErrors{
        facade.register("jacob", "pass", "jacob@gmail.com");
        Assertions.assertThrows(ResponseErrors.class, () -> facade.login("jacob", "pass2"),
                "Trying to login with incorrect password");
    }

    @Test
    @DisplayName("logoutGood")
    public void logoutGood() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");
        facade.logout(data.authToken());
        Assertions.assertThrows(ResponseErrors.class, () -> facade.logout(data.authToken()),
                "failed to logout");


    }

    @Test
    @DisplayName("logoutBad")
    public void logoutBad() throws ResponseErrors{
        facade.register("jacob", "pass", "jacob@gmail.com");
        Assertions.assertThrows(ResponseErrors.class, () -> facade.logout("bad"),
                "Trying to logout wrong user");
    }

    @Test
    @DisplayName("createGameGood")
    public void createGameGood() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");
        var game = facade.createGame(data.authToken(),"game");
        Assertions.assertTrue(game > 0, "failed to create game");


    }

    @Test
    @DisplayName("createGameBad")
    public void createGameBad() throws ResponseErrors{
        facade.register("jacob", "pass", "jacob@gmail.com");
        Assertions.assertThrows(ResponseErrors.class, () -> facade.createGame("bad", "game"),
                "created game with a bad token");
    }

    @Test
    @DisplayName("listGameGood")
    public void listGameGood() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");
        var game1 = facade.createGame(data.authToken(),"game1");
        var game2 = facade.createGame(data.authToken(),"game2");
        Assertions.assertTrue(facade.listGames(data.authToken()).size() == 2, "failed to list games");


    }

    @Test
    @DisplayName("listGameBad")
    public void listGameBad() throws ResponseErrors{
        Assertions.assertThrows(ResponseErrors.class, () -> facade.listGames("bad"),
                "listed games with a bad token");
    }

    @Test
    @DisplayName("joinGameGood")
    public void joinGameGood() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");
        var game = facade.createGame(data.authToken(),"game2");

        Assertions.assertDoesNotThrow(() -> facade.joinGame(data.authToken(), "WHITE", game), "failed to join games");


    }

    @Test
    @DisplayName("joinGameBad")
    public void joinGameBad() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");
        var game = facade.createGame(data.authToken(),"game2");
        facade.joinGame(data.authToken(), "WHITE", game);
        Assertions.assertThrows(ResponseErrors.class, () -> facade.joinGame(data.authToken(), "WHITE", game),
                "joined a color already taken");
    }

    @Test
    @DisplayName("clear")
    public void clearTest() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");
        facade.clear();
        Assertions.assertThrows(ResponseErrors.class, () -> facade.login("jacob", "pass"),
                "user failed to clear");
    }






}
