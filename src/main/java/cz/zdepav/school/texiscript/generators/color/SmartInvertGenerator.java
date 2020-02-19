package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class SmartInvertGenerator extends Generator {

    private final Generator base;

    public SmartInvertGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).smartNegate();
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
    public boolean isColor() {
        return base.isColor();
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
