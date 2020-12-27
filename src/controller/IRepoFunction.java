package controller;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.Connection;

public interface IRepoFunction {

    Response addSingleImage(InputStream path, String username, Permission permission, String[] charecteristics, Connection connection);

    Response addMultipleImages(InputStream[] paths, String username, Permission permission, String[] charecteristics, Connection connection);

}

