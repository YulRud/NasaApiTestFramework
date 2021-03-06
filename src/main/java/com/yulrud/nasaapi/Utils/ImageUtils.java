package com.yulrud.nasaapi.Utils;

import com.yulrud.nasaapi.TestLogger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ImageUtils {
    public static BufferedImage openImageFromFile(String path) {
        BufferedImage bImage = null;

        try {
            bImage = ImageIO.read(new File(path));
        } catch (IOException e) {
            TestLogger.log(String.format("Not able to open/read an image by path: %s", path));
            e.printStackTrace();
        }
        TestLogger.log(String.format("The read an image by path was read: %s", path));
        return bImage;
    }

    public static boolean compareImagesByPixels(BufferedImage pic1, BufferedImage pic2) {
        byte[] pixelsPic1 = ((DataBufferByte) pic1.getRaster().getDataBuffer()).getData();
        byte[] pixelsPic2 = ((DataBufferByte) pic2.getRaster().getDataBuffer()).getData();
        return Arrays.equals(pixelsPic1, pixelsPic2);
    }
}
