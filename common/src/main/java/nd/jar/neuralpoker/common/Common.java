package nd.jar.neuralpoker.common;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import static java.lang.Math.ceil;

public class Common {
    public final static int topLeftCornerX[] = {143, 214, 286, 357, 430};
    public final static int topLeftCornerY[] = {585, 617};
    public final static int numCoordinates[] = {4, 4};
    public final static int numSize[] = {33, 26};//w,h
    public final static int suitCoordinates[] = {25, 48}; // x,y
    public final static int suitSize[] = {33, 35};//w,h

    public static SizedBitSet getNthSizedBitSet(BufferedImage initImage, int i, int[] coordinates, int[] size) {
        final var suitImg = initImage.getSubimage(topLeftCornerX[i] + coordinates[0], topLeftCornerY[0] + coordinates[1], size[0], size[1]);
        return toResizedBitArray(suitImg);
    }


    public static SizedBitSet toResizedBitArray(BufferedImage image) {
        final var w = (int) ceil(image.getWidth() / 2.0);
        final var h = (int) ceil(image.getHeight() / 2.0);
        double[] out = new double[w*h];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                var result = mapToInt(image, col * 2, row * 2) +
                        mapToInt(image, col * 2 + 1, row * 2) +
                        mapToInt(image, col * 2, row * 2 + 1) +
                        mapToInt(image, col * 2 + 1, row * 2 + 1);
                if (result >= 2) {
                    out[row * w + col] = 1.0;
                } else {
                    out[row * w + col] = 0.0;
                }
            }
        }
        return new SizedBitSet(out, w, h);
    }
    private static int mapToInt(BufferedImage image, int col, int row) {
        if (col >= image.getWidth() || row >= image.getHeight()) {
            return 0;
        }
        var rgb = image.getRGB(col, row);
        return isSet(new Color(rgb)) ? 1 : 0;
    }
    private static final int threshold = 50;

    private static boolean isSet(Color color) {
        return (color.getRed() > color.getBlue() &&
                color.getRed() > color.getGreen()) || (color.getRed() < threshold &&
                color.getBlue() < threshold &&
                color.getGreen() < threshold);
    }
    public record SizedBitSet(double[] set, int width, int height) {
        public boolean isEmpty() {
            var sum = Arrays.stream(set).sum();
            return sum < 30 || sum > width * height - 30;
        }
    }
}