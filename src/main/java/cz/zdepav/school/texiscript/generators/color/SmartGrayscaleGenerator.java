package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Converts its input to grayscale while taking into account different perception of color channels by a human eye. */
public class SmartGrayscaleGenerator extends Generator {

    /** input generator */
    private final Generator base;

    public SmartGrayscaleGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var col = base.getColor(x, y);
        var intensity = 0.3 * col.r + 0.59 * col.g + 0.11 * col.b;
        return new RgbaColor(intensity, intensity, intensity, col.a);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var col = base.getColor(x, y);
        return 0.3 * col.r + 0.59 * col.g + 0.11 * col.b;
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        var col = base.getColor(x, y);
        return Utils.lerp(min, max, 0.3 * col.r + 0.59 * col.g + 0.11 * col.b);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isColor() {
        return base.isColor();
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
