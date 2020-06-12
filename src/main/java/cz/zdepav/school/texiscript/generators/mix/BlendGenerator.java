package cz.zdepav.school.texiscript.generators.mix;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Blends two inputs using alpha blending. */
public class BlendGenerator extends Generator {

    private final Generator a, b;

    public BlendGenerator(CodePosition pos, Generator a, Generator b) {
        this.a = a;
        this.b = b;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return a.getColor(x, y).blend(b.getColor(x, y));
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return getColor(x, y).intensity;
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, getColor(x, y).intensity);
    }

    public boolean isColor() {
        return a.isColor() && b.isColor();
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        a.init(outputSize, randomize);
        b.init(outputSize, randomize);
    }
}
