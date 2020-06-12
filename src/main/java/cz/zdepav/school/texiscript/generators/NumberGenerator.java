package cz.zdepav.school.texiscript.generators;

import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Basic generator with fixed numeric value. */
public class NumberGenerator extends Generator {

    /** generated value */
    private final double number;

    NumberGenerator(double number) {
        this.number = number;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(number);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return number;
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.clamp(number, min, max);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) { }
}
