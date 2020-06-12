package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Converts its input to grayscale. */
public class GrayscaleGenerator extends Generator {

    /** input generator */
    private final Generator base;

    public GrayscaleGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var col = base.getColor(x, y);
        return new RgbaColor(col.intensity, col.intensity, col.intensity, col.a);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return base.getDouble(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return base.getDouble(x, y, min, max);
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
