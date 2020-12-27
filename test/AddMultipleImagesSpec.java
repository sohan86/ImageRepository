import controller.Permission;
import controller.RepoFunction;
import controller.Response;
import controller.Util;
import org.junit.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class AddMultipleImagesSpec {

    private static Util log;
    private static RepoFunction rf;
    private static BufferedImage[] images;

    @BeforeClass
    public static void before() {
        log = new Util();
        log.test("Before: Add Multiple Images Spec");
        File imagesDir = new File("test/images");
        int length = imagesDir.list().length - 1;
        images = new BufferedImage[length];
        int count = 0;
        for (File subFile : imagesDir.listFiles()) {
            try {
                BufferedImage img = ImageIO.read(subFile);
                if (img != null) {
                    images[count] = img;
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
            File dir = new File("src/data/sohan42");
            dir.mkdir();
            File images = new File(dir + "/images");
            images.mkdir();
        } catch (Exception e) {
            e.printStackTrace();
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
    public void addMultipleImages() {
        log.test("Adding Multiple Images");
        Response resp = rf.addMultipleImages(images, "sohan42", Permission.PUBLIC);
        Assert.assertEquals(200, resp.code);
        log.test(resp.body);
    }

    @Test
    public void addMultipleImagesWithOneNull() {
        log.test("Adding Multiple Images With One Null Image");
        images[images.length - 1] = null;
        Response resp = rf.addMultipleImages(images, "sohan42", Permission.PUBLIC);
        Assert.assertEquals(400, resp.code);
        log.test(resp.body);
    }

}
