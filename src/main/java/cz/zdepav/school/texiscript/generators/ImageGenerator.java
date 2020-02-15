package cz.zdepav.school.texiscript.generators;

import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

import java.awt.image.BufferedImage;

/** @author Zdenek Pavlatka */
public class ImageGenerator extends Generator {

    private final BufferedImage image;
    private final int width, height;

    ImageGenerator(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        // uses linear interpolation, texture is tiled
        var _x = x * width;
        var _y = y * height;
        var x0 = Utils.wrap((int)_x, width);
        var y0 = Utils.wrap((int)_y, height);
        var x1 = (x0 + 1) % width;
        var y1 = (y0 + 1) % height;
        var xd = Utils.wrap(_x, width) % 1;
        var yd = Utils.wrap(_y, width) % 1;
        var c00 = RgbaColor.fromArgbPixel(image.getRGB(x0, y0));
        var c01 = RgbaColor.fromArgbPixel(image.getRGB(x0, y1));
        var c10 = RgbaColor.fromArgbPixel(image.getRGB(x1, y0));
        var c11 = RgbaColor.fromArgbPixel(image.getRGB(x1, y1));
        return c00.lerp(c10, xd).lerp(c01.lerp(c11, xd), yd);
    }

    @Override
    public double getDouble(double x, double y) {
        return getColor(x, y).intensity;
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, getColor(x, y).intensity);
    }

    @Override
    public void init(int outputSize, boolean randomize) { }
}
