import controller.Permission;
import controller.RepoFunction;
import controller.Response;
import controller.Util;
import login.User;
import org.junit.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class AddSingleImageSpec {

    private static RepoFunction rf;
    private static Util log;
    private static BufferedImage singleImg;
    private static String[] characteristics;

    @BeforeClass
    public static void before() {
        log = new Util();
        log.test("Before: Add Single Image Spec");
        characteristics = new String[3];
        characteristics[0] = "cartoon";
        characteristics[1] = "spongebob";
        characteristics[2] = "reflective";
        singleImg = null;
        try {
            singleImg = ImageIO.read(new File("test/images/spongebob.jpg"));
            log.trace("Read Single Image");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void beforeEachTest() {
        log.test("--Starting Next Test");
        rf = new RepoFunction();
        try {
            User user = new User("sohan42", "Sohan1234*", "Sohan");
            Response resp = user.createUser();
            log.test(resp.body);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
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
    public void addNullImage() {
        log.test("Adding Null Image");
        Response resp = rf.addSingleImage(null, "sohan42", Permission.PUBLIC, characteristics);
        Assert.assertEquals(400, resp.code);
        log.test(resp.body);
    }

    @Test
    public void addSingleImage() {
        log.test("Adding Single Image");
        Response resp = rf.addSingleImage(singleImg, "sohan42", Permission.PUBLIC, characteristics);
        Assert.assertEquals(200, resp.code);
        log.test(resp.body);
    }

}
