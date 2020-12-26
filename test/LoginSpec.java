import controller.Response;
import controller.Util;
import login.Login;
import login.User;
import org.junit.*;

import java.io.File;

public class LoginSpec {

    private static Util log;
    private Login login;
    private User user;
    private String legalUsername = "sohan42";
    private String legalPassword = "Sohan1234*";
    private String legalName = "Sohan";

    @BeforeClass
    public static void before() {
        log = new Util();
        log.test("Before: Login Spec");
    }

    @Before
    public void beforeEach() {
        log.test("--Starting Next Test");
    }

    @After
    public void afterEach() {
        File dir = new File("src/data");
        for (File subFile : dir.listFiles()) {
            if (subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        log.test("--Finished a Test");
    }

    static void deleteFolder(File file){
        for (File subFile : file.listFiles()) {
            if(subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        file.delete();
    }

    @Test
    public void loginWithUser() {
        log.test("Logging In With a Valid Username and Password");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp1 = user.createUser();
        Assert.assertEquals(200, resp1.code);
        log.test(resp1.body);
        login = new Login();
        Response resp2 = login.login(legalUsername, legalPassword);
        Assert.assertEquals(202, resp2.code);
        log.test(resp2.body);
    }

    @Test
    public void loginWithInvalidPassword() {
        log.test("Logging In With a Valid Username and Invalid Password");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp1 = user.createUser();
        Assert.assertEquals(200, resp1.code);
        log.test(resp1.body);
        login = new Login();
        Response resp2 = login.login(legalUsername, "Sohan1234");
        Assert.assertEquals(400, resp2.code);
        log.test(resp2.body);
    }

    @Test
    public void loginWithInvalidUsername() {
        log.test("Logging In With a Invalid Username");
        user = new User(legalUsername, legalPassword, legalName);
        Response resp1 = user.createUser();
        Assert.assertEquals(200, resp1.code);
        log.test(resp1.body);
        login = new Login();
        Response resp2 = login.login("sohan43", legalPassword);
        Assert.assertEquals(400, resp2.code);
        log.test(resp2.body);
    }

    @Test
    public void loginWithNoUsers() {
        log.test("Logging In With No Existing Users");
        login = new Login();
        Response resp2 = login.login(legalUsername, legalPassword);
        Assert.assertEquals(400, resp2.code);
        log.test(resp2.body);
    }

}
