package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Computes absolute value of its input. */
public class AbsoluteValueGenerator extends Generator {

    /** input generator */
    private final Generator base;

    public AbsoluteValueGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).abs();
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return Math.abs(base.getDouble(x, y));
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return base.isNumber();
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