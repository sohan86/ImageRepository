import controller.Permission;
import controller.RepoFunction;
import controller.Response;
import controller.Util;
import login.User;
import org.junit.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSingleImageSpec {

    private static RepoFunction rf;
    private static Util log;
    private static String singleImg;
    private static String[] characteristics;
    private static Connection connection;
    private static PreparedStatement preparedStmt;
    private static InputStream in;

    @BeforeClass
    public static void before() {
        connection = App.connectDatabase();
        log = new Util();
        log.test("Before: Add Single Image Spec");
        characteristics = new String[3];
        characteristics[0] = "cartoon";
        characteristics[1] = "spongebob";
        characteristics[2] = "reflective";
        singleImg = "test/images/spongebob.jpg";
        try {
            in = new FileInputStream(singleImg);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        log.trace("Read Single Image");
    }

    @Before
    public void beforeEachTest() {
        log.test("--Starting Next Test");
        rf = new RepoFunction();
        try {
            User user = new User("sohan42", "Sohan1234*", "Sohan");
            Response resp = user.createUser(connection);
            log.test(resp.body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    public void addSingleImage() {
        log.test("Adding Single Image");
        Response resp = rf.addSingleImage(in, "sohan42", Permission.PUBLIC, characteristics, connection);
        Assert.assertEquals(200, resp.code);
        log.test(resp.body);
    }

}
