import controller.Response;
import controller.Util;
import login.Login;
import login.User;
import org.junit.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoginSpec {

    private static Util log;
    private static Connection connection;
    private static PreparedStatement preparedStmt;
    private Login login;
    private User user;
    private String legalUsername = "sohan42";
    private String legalPassword = "Sohan1234*";
    private String legalName = "Sohan";

    @BeforeClass
    public static void before() {
        connection = App.connectDatabase();
        log = new Util();
        log.test("Before: Login Spec");
    }

    @Before
    public void beforeEach() {
        log.test("--Starting Next Test");
    }

    @After
    public void afterEach() {
        String query = "delete from users";
        try {
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        log.test("--Finished a Test");
    }

    @AfterClass
    public static void after(){
        try {
            preparedStmt.close();
            connection.close();
            log.test("After: Login Spec");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void loginWithUser() {
        log.test("Logging In With a Valid Username and Password");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp1 = user.createUser(connection);
        Assert.assertEquals(200, resp1.code);
        log.test(resp1.body);
        login = new Login();
        Response resp2 = login.login(legalUsername, legalPassword, connection);
        Assert.assertEquals(202, resp2.code);
        log.test(resp2.body);
    }

    @Test
    public void loginWithInvalidPassword() {
        log.test("Logging In With a Valid Username and Invalid Password");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp1 = user.createUser(connection);
        Assert.assertEquals(200, resp1.code);
        log.test(resp1.body);
        login = new Login();
        Response resp2 = login.login(legalUsername, "Sohan1234", connection);
        Assert.assertEquals(400, resp2.code);
        log.test(resp2.body);
    }

    @Test
    public void loginWithInvalidUsername() {
        log.test("Logging In With a Invalid Username");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp1 = user.createUser(connection);
        Assert.assertEquals(200, resp1.code);
        log.test(resp1.body);
        login = new Login();
        Response resp2 = login.login("sohan43", legalPassword, connection);
        Assert.assertEquals(400, resp2.code);
        log.test(resp2.body);
    }

    @Test
    public void loginWithNoUsers() {
        log.test("Logging In With No Existing Users");
        login = new Login();
        Response resp2 = login.login(legalUsername, legalPassword, connection);
        Assert.assertEquals(400, resp2.code);
        log.test(resp2.body);
    }

}
