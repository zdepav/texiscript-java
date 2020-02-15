package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class AbsoluteValueGenerator extends Generator {

    private final Generator base;

    public AbsoluteValueGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).abs();
    }

    @Override
    public double getDouble(double x, double y) {
        return Math.abs(base.getDouble(x, y));
    }

    @Override
    public boolean isNumber() {
        return base.isNumber();
    }

    @Override
    public boolean isColor() {
        return base.isColor();
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}