package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Adds two values. */
public class AdditionGenerator extends Generator {

    private final Generator a, b;

    public AdditionGenerator(Generator a, Generator b) {
        this.a = a;
        this.b = b;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return a.getColor(x, y).add(b.getColor(x, y));
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return a.getDouble(x, y) + b.getDouble(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, a.getDouble(x, y) + b.getDouble(x, y));
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return a.isNumber() && b.isNumber();
    }

    /** {@inheritDoc} */
    @Override
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
