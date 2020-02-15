package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class NormalizationGenerator extends Generator {

    private final Generator base;

    public NormalizationGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).normalize();
    }

    @Override
    public double getDouble(double x, double y) {
        return Utils.clamp(base.getDouble(x, y), 0, 1);
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, base.getDouble(x, y));
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
