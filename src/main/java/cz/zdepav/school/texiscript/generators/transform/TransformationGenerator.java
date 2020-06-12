package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Base class for transformations. */
public abstract class TransformationGenerator extends Generator {

    private final Generator base;

    public TransformationGenerator(Generator base) {
        this.base = base;
    }

    protected abstract Vec2 reverseTransform(double x, double y);

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var v = this.reverseTransform(x, y);
        return base.getColor(v.x, v.y);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var v = this.reverseTransform(x, y);
        return base.getDouble(v.x, v.y);
    }

    public boolean isNumber() {
        return base.isNumber();
    }

    public boolean isColor() {
        return base.isColor();
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
