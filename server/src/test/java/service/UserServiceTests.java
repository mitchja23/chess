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
    @DisplayName("register duplicate username")
    public void registerAlreadyTaken() {
        Assertions.assertThrows(AlreadyTakenException.class,
                () -> service.register(existingUser),
                "registered a taken username");
    }

    @Test
    @DisplayName("register missing fields")
    public void registerBadRequest() {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.register(new Request.RegisterRequest("jacob3", null, "jacob3@mail.com")),
                "registered with missing password");
    }

    @Test
    @DisplayName("login missing fields")
    public void loginBadRequest() {
        Assertions.assertThrows(BadRequestException.class,
                () -> service.login(new Request.LoginRequest("jacob", null)),
                "logged in with missing password");
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
