package cz.zdepav.school.texiscript.generators;

import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class ColorGenerator extends Generator {

    private RgbaColor color;

    ColorGenerator(RgbaColor color) { this.color = color; }

    @Override
    public RgbaColor getColor(double x, double y) { return color; }

    @Override
    public double getDouble(double x, double y) { return color.intensity; }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, getColor(x, y).intensity);
    }

    public boolean isColor() { return true; }

    @Override
    public void init(int outputSize, boolean randomize) { }
}
