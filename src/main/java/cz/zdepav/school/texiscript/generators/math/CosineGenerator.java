package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Angle;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class CosineGenerator extends Generator {

    private final Generator base;

    public CosineGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(getDouble(x, y));
    }

    @Override
    public double getDouble(double x, double y) {
        return Math.cos(base.getDouble(x, y) * Angle.deg360) / 2 + 0.5;
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
