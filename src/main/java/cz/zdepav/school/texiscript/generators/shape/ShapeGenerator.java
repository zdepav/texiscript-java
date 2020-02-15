package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public abstract class ShapeGenerator extends Generator {

    private final Generator color, background;

    protected ShapeGenerator(Generator color, Generator background) {
        this.color = color;
        this.background = background;
    }

    protected abstract boolean isInside(double x, double y);

    @Override
    public RgbaColor getColor(double x, double y) {
        return isInside(x, y)
            ? color.getColor(x, y)
            : background.getColor(x, y);
    }

    @Override
    public double getDouble(double x, double y) {
        return isInside(x, y) ? color.getDouble(x, y) : background.getDouble(x, y);
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return isInside(x, y) ? color.getDouble(x, y, min, max) : background.getDouble(x, y, min, max);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        color.init(outputSize, randomize);
        background.init(outputSize, randomize);
    }
}
