package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class InvertingGenerator extends Generator {

    private final Generator base;

    public InvertingGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).negate();
    }

    @Override
    public double getDouble(double x, double y) {
        return 1 - base.getDouble(x, y);
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return base.getDouble(x, y, max, min);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
