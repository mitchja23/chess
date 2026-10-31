package dataaccess.sql;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.*;



public class SqlUserDAOTest {

    private UserDAO userDAO;

    @BeforeEach
    public void setup() throws Exception{
        userDAO = new SqlUserDAO();
        userDAO.clear();
    }


    @Test
    @DisplayName("createUser")
    public void createUser() throws Exception {
        var data = new UserData("jacob", "pass", "jacob@gmail.com");
        userDAO.createUser(data);

        var result = userDAO.getUser("jacob");
        Assertions.assertNotNull(result, "failed to create user");
        Assertions.assertEquals(data.username(), result.username(), "wrong username");
        Assertions.assertEquals(data.email(), result.email(), "wrong email");
    }

    @Test
    @DisplayName("createUserBad")
    public void createUserBad() throws Exception {
        var user1 = new UserData("jacob", "pass", "jacob1@gmail.com");
        var user2 = new UserData("jacob", "pass", "jacob2@gmail.com");
        userDAO.createUser(user1);
        Assertions.assertThrows(DataAccessException.class,
                () -> userDAO.createUser(user2),
                "created a user with a dup name");

    }

    @Test
    @DisplayName("getUser")
    public void getUser() throws Exception {
        var data = new UserData("jacob", "pass", "jacob@gmail.com");
        userDAO.createUser(data);

        var result = userDAO.getUser("jacob");
        Assertions.assertNotNull(result, "failed to get user");
        Assertions.assertEquals(data.username(), result.username(), "wrong username");
        Assertions.assertEquals(data.email(), result.email(), "wrong email");
    }

    @Test
    @DisplayName("getUserBad")
    public void getUserBad() throws Exception {
        var result = userDAO.getUser("jacob");
        Assertions.assertNull(result, "User does not exist");
    }

    @Test
    @DisplayName("clear")
    public void clear() throws Exception {
        userDAO.clear();
        Assertions.assertNull(userDAO.getUser("jacob"),"user was not cleared");
    }

}


