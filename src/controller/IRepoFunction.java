package controller;

import java.awt.image.BufferedImage;

public interface IRepoFunction {

    Response addSingleImage(BufferedImage img, String username);

    Response addMultipleImages(BufferedImage[] images, String username);

}

