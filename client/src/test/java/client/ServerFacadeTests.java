package client;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        facade = new ServerFacade(port);
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear() throws ResponseErrors{
        facade.clear()
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("resgisterGood")
    public void resgisterGood() throws ResponseErrors{
        var data = facade.register("jacob", "pass", "jacob@gmail.com");

        Assertions.assertNotNull(data, "failed to register");
        Assertion.assertEquals("jacob". data.username(). "usernames dont match");
        Assertions.assertNotNull(data.authToken(), "no auth token");

    }

    @Test
    @DisplayName("registerBad")
    public void registerBad() throws ResponseErrors{
        facade.register("jacob", "pass", "jacob@gmail.com");
        Assertions.assertThrows(ReponseErrors.class() -> facade.register("jacob", "pass", "jacob2@gmail.com"),
        "Trying to register with a taken name")
    }




}
