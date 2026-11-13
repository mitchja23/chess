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




}
