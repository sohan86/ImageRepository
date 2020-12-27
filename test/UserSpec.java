import controller.Response;
import controller.Util;
import login.User;
import org.junit.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserSpec {

    private static Util log;
    private static Connection connection;
    private static PreparedStatement preparedStmt;
    private User user;
    private String legalUsername = "sohan42";
    private String legalPassword = "Sohan1234*";
    private String legalName = "Sohan";

    @BeforeClass

    public static void before() {
        connection = App.connectDatabase();
        log = new Util();
        log.test("Before: User Spec");
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
    public static void after() {
        try {
            preparedStmt.close();
            connection.close();
            log.test("After: User Spec");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void createLegalUser() {
        log.test("Creating a Legal User");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp = user.createUser(connection);
        Assert.assertEquals("Sohan", user.getName());
        Assert.assertEquals("sohan42", user.getUsername());
        Assert.assertEquals("Sohan1234*", user.getPassword());
        Assert.assertEquals(200, resp.code);
        log.test(resp.body);
    }

    @Test
    public void createUserIllegalName() {
        log.test("Creating User with Illegal Name");
        user = new User(legalUsername, legalPassword, "S0han");
        Response resp = user.createUser(connection);
        Assert.assertEquals(400, resp.code);
        log.test(resp.body);
    }

    @Test
    public void createUserBlankName() {
        log.test("Creating User with Blank Name");
        user = new User(legalUsername, legalPassword, "");
        Response resp = user.createUser(connection);
        Assert.assertEquals(400, resp.code);
        log.test(resp.body);
    }

    @Test
    public void createUserBlankUsername() {
        log.test("Creating User with Blank Username");
        user = new User("", legalPassword, legalName);
        Response resp = user.createUser(connection);
        Assert.assertEquals(400, resp.code);
        log.test(resp.body);
    }

    @Test
    public void createUserBlankPassword() {
        log.test("Creating User with Blank Password");
        user = new User(legalUsername, "", legalName);
        Response resp = user.createUser(connection);
        Assert.assertEquals(400, resp.code);
        log.test(resp.body);
    }

    @Test
    public void createUserIdenticalUsername() {
        log.test("Creating User with Identical Username");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp1 = user.createUser(connection);
        Assert.assertEquals(200, resp1.code);
        log.test(resp1.body);
        User duplicate = new User(legalUsername, "password", "Mathew");
        Response resp2 = duplicate.createUser(connection);
        Assert.assertEquals(400, resp2.code);
        log.test(resp2.body);
    }

}
