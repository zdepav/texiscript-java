package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Base class for shape generators. */
public abstract class ShapeGenerator extends Generator {

    private final Generator color, background, bevel, curve;

    protected ShapeGenerator(Generator color, Generator background, Generator bevel, Generator curve) {
        this.color = color;
        this.background = background;
        this.bevel = bevel;
        this.curve = curve;
    }

    /** distance is normalited to [0,1] range, must return a value greater than one when outside of the shape */
    protected abstract double distanceFromCenter(double x, double y);

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var dist = distanceFromCenter(x, y);
        var b = Utils.clamp(bevel.getDouble(x, y), 0.0, 1.0);
        var rb = 1 - b;
        if (dist <= rb) {
            return color.getColor(x, y);
        } else if (dist >= 1.0) {
            return background.getColor(x, y);
        } else {
            dist = 1 - curve.getDouble(1 - (dist - rb) / b, y);
            return color.getColor(x, y).lerp(background.getColor(x, y), dist);
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var dist = distanceFromCenter(x, y);
        var b = Utils.clamp(bevel.getDouble(x, y), 0.0, 1.0);
        var rb = 1 - b;
        if (dist <= rb) {
            return color.getDouble(x, y);
        } else if (dist >= 1.0) {
            return background.getDouble(x, y);
        } else {
            dist = curve.getDouble((dist - rb) / b, y);
            return Utils.lerp(color.getDouble(x, y), background.getDouble(x, y), dist);
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        var dist = distanceFromCenter(x, y);
        var b = Utils.clamp(bevel.getDouble(x, y), 0.0, 1.0);
        var rb = 1 - b;
        if (dist <= rb) {
            return color.getDouble(x, y, min, max);
        } else if (dist >= 1.0) {
            return background.getDouble(x, y, min, max);
        } else {
            dist = curve.getDouble((dist - rb) / b, y);
            return Utils.lerp(color.getDouble(x, y, min, max), background.getDouble(x, y, min, max), dist);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        color.init(outputSize, randomize);
        background.init(outputSize, randomize);
        bevel.init(outputSize, randomize);
        curve.init(outputSize, randomize);
    }
}
