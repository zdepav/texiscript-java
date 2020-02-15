package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class RandomGenerator extends Generator {

    private final Generator base;

    public RandomGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var i = getRandom(base.getDouble(x, y), 0.5).r();
        return new RgbaColor(i);
    }

    @Override
    public double getDouble(double x, double y) {
        return getRandom(base.getDouble(x, y), 0.5).r();
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
