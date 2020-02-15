package cz.zdepav.school.texiscript.generators.mix;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class BlendGenerator extends Generator {

    private final Generator a, b;

    public BlendGenerator(CodePosition pos, Generator a, Generator b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return a.getColor(x, y).blend(b.getColor(x, y));
    }

    @Override
    public double getDouble(double x, double y) {
        return a.getDouble(x, y) + b.getDouble(x, y);
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, a.getDouble(x, y) + b.getDouble(x, y));
    }

    public boolean isColor() {
        return a.isColor() && b.isColor();
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        a.init(outputSize, randomize);
        b.init(outputSize, randomize);
    }
}
