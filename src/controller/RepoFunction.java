package controller;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RepoFunction implements IRepoFunction{

    @Override
    public Response addSingleImage(BufferedImage img, String username, Permission permission) {
        Response resp = new Response();
        if (img == null) {
            resp.code = 400;
            resp.body = "Null Image Added";
        } else {
            try {
                String path = "src/data/" + username + "/images";
                File dirImages = new File(path);
                int count = dirImages.list().length + 1;
                String order = Integer.toString(count);
                path = path + "/" + order;
                File imageDir = new File(path);
                boolean boolImageDir = imageDir.mkdir();
                if (boolImageDir) {
                    String imagePath = path + "/" + order + ".png";
                    ImageIO.write(img, "jpg", new File(imagePath));
                    String detailsPath = path + "/" + "details.txt";
                    File details = new File(detailsPath);
                    boolean boolDetails = details.createNewFile();
                    if (boolDetails) {
                        String access;
                        switch (permission) {
                            case PUBLIC:
                                access = "public";
                                break;
                            case PRIVATE:
                                access = "private";
                                break;
                            default:
                                access = "";
                        }
                        writeDetails(detailsPath, username, order, access);
                        resp.code = 200;
                        resp.body = "Image Successfully Uploaded";
                    } else {
                        resp.code = 400;
                        resp.body = "Image details file could not be created";
                    }
                } else {
                    resp.code = 400;
                    resp.body = "Image directory could not be created";
                }
            } catch (IOException e) {
                e.printStackTrace();
                resp.code = 400;
                resp.body = e.getMessage();
                e.printStackTrace();
            }
        }
        return resp;
    }

    private void writeDetails(String path, String username, String order, String access) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(username);
        writer.write("\r\n");
        writer.write(order);
        writer.write("\r\n");
        writer.write(access);
        writer.close();
    }

    @Override
    public Response addMultipleImages(BufferedImage[] images, String username, Permission permission) {
        Response finalResp = new Response();
        int count = 1;
        for (BufferedImage img : images) {
            Response resp = addSingleImage(img, username, permission);
            if (resp.code == 400) {
                finalResp.code = resp.code;
                finalResp.body = resp.body;
                deleteInvalidImages(username, count);
                return finalResp;
            }
            count++;
        }
        finalResp.code = 200;
        finalResp.body = "Images Uploaded Successfully";
        return finalResp;
    }

    private void deleteInvalidImages(String username, int count) {
        String path = "src/data/" + username + "/images";
        File imagesDir = new File(path);
        if (imagesDir.list().length != 0) {
            File[] images = imagesDir.listFiles();
            for (int i = images.length - 1; i >= 0; i--) {
                if (count == 0) {
                    break;
                } else {
                    deleteFolder(images[i]);
                    count--;
                }
            }
        }
    }

    private void deleteFolder(File image) {
        for (File subFile : image.listFiles()) {
            if(subFile.isDirectory()) {
                deleteFolder(subFile);
            } else {
                subFile.delete();
            }
        }
        image.delete();
    }

}
