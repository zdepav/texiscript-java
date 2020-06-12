package cz.zdepav.school.texiscript.generators;

import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Basic generator with fixed color value. */
public class ColorGenerator extends Generator {

    /** generated color */
    private final RgbaColor color;

    ColorGenerator(RgbaColor color) { this.color = color; }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) { return color; }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) { return color.intensity; }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, getColor(x, y).intensity);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isColor() { return true; }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) { }
}
