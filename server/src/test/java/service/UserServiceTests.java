package service;

import dataaccess.*;
import org.junit.jupiter.api.*;

public class UserServiceTests {

    private AuthDAO authDAO;
    private UserService service;
    private Request.RegisterRequest existingUser;

    @BeforeEach
    public void setup() throws Exception {
        authDAO = new MemoryAuthDAO();
        service = new UserService(new MemoryUserDAO(), authDAO);
        existingUser = new Request.RegisterRequest("jacob", "password", "jacob@mail.com");
        service.register(existingUser);
    }

    @Test
    @DisplayName("register")
    public void registerSuccess() throws Exception {
        var result = service.register(new Request.RegisterRequest("jacob2", "password", "jacob2@mail.com"));
        Assertions.assertNotNull(authDAO.getAuth(result.authToken()), "failed to register");
    }

    @Test
    @DisplayName("login")
    public void loginSuccess() throws Exception {
        var result = service.login(new Request.LoginRequest("jacob", "password"));

        Assertions.assertNotNull(authDAO.getAuth(result.authToken()), "failed to login");
    }

    @Test
    @DisplayName("unauthorized login")
    public void loginUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class,
                () -> service.login(new Request.LoginRequest("jacob", "wrong")),
                "failed to login");
    }

    @Test
    @DisplayName("logout")
    public void logoutSuccess() throws Exception {
        var result = service.login(new Request.LoginRequest("jacob", "password"));
        service.logout(result.authToken());
        Assertions.assertNull(authDAO.getAuth(result.authToken()), "failed ot logout");
    }

    @Test
    @DisplayName("failed logout")
    public void logoutUnauthorized() {
        Assertions.assertThrows(UnauthorizedException.class, () -> service.logout("bad"),
                "failed ot logout");
    }
}
