package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Inverts its input mathematically. */
public class InvertingGenerator extends Generator {

    /** input generator */
    private final Generator base;

    public InvertingGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).negate();
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return 1 - base.getDouble(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return base.getDouble(x, y, max, min);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isColor() {
        return base.isColor();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return base.isNumber();
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
