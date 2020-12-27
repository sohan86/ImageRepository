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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.ConsoleHandler;

public class AddMultipleImagesSpec {

    private static Util log;
    private static RepoFunction rf;
    private static InputStream[] images;
    private static String[] characteristics;
    private static Connection connection;
    private static PreparedStatement preparedStmt;

    @BeforeClass
    public static void before() {
        connection = App.connectDatabase();
        log = new Util();
        log.test("Before: Add Multiple Images Spec");
        characteristics = new String[2];
        characteristics[0] = "cartoon";
        characteristics[1] = "colorful";
        File imagesDir = new File("test/images");
        int length = imagesDir.list().length;
        images = new InputStream[length];
        int count = 0;
        for (File subFile : imagesDir.listFiles()) {
            try {
                BufferedImage img = ImageIO.read(subFile);
                if (img != null) {
                    images[count] = new FileInputStream(subFile.getAbsolutePath());
                    count++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.trace("Read Multiple Images");
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
    public void addMultipleImages() {
        log.test("Adding Multiple Images");
        Response resp = rf.addMultipleImages(images, "sohan42", Permission.PUBLIC, characteristics, connection);
        Assert.assertEquals(200, resp.code);
        log.test(resp.body);
    }

}
