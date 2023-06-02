package ru.brikster.glyphs.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageUtil {

    public int calculateWidth(BufferedImage image, int xFrom, int yFrom, int xTo, int yTo) {
        int width;
        for (width = xTo - 1; width > xFrom; width--) {
            for (int height = yFrom; height < yTo; height++) {
                if (new Color(image.getRGB(width, height), true).getAlpha() != 0) {
                    return width - xFrom + 1;
                }
            }
        }

        return width - xFrom + 1;
    }

    public int calculateWidth(BufferedImage image) {
        return calculateWidth(image, 0, 0, image.getWidth(), image.getHeight());
    }
}
