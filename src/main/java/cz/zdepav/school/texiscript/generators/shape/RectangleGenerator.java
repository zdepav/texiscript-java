package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class RectangleGenerator extends ShapeGenerator {

    private final Generator x, y, width, height;

    public RectangleGenerator(
        CodePosition pos,
        Generator color, Generator background,
        Generator x, Generator y,
        Generator width, Generator height
    ) {
        super(color, background);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    protected boolean isInside(double x, double y) {
        x -= this.x.getDouble(x, y);
        y -= this.y.getDouble(x, y);
        return x >= 0 && y >= 0 && x < width.getDouble(x, y) && y < height.getDouble(x, y);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        width.init(outputSize, randomize);
        height.init(outputSize, randomize);
    }
}
