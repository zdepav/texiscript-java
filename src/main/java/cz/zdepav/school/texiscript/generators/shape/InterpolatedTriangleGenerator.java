package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Generates a triangle with colors interpolated between its vertices. */
public class InterpolatedTriangleGenerator extends Generator {

    private final Generator background, x1, y1, color1, x2, y2, color2, x3, y3, color3;

    public InterpolatedTriangleGenerator(
        CodePosition pos, Generator background,
        Generator x1, Generator y1, Generator color1,
        Generator x2, Generator y2, Generator color2,
        Generator x3, Generator y3, Generator color3
    ) {
        this.background = background;
        this.x1 = x1;
        this.y1 = y1;
        this.color1 = color1;
        this.x2 = x2;
        this.y2 = y2;
        this.color2 = color2;
        this.x3 = x3;
        this.y3 = y3;
        this.color3 = color3;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var a = new Vec2(x1.getDouble(x, y), y1.getDouble(x, y));
        var b = new Vec2(x2.getDouble(x, y), y2.getDouble(x, y));
        var c = new Vec2(x3.getDouble(x, y), y3.getDouble(x, y));
        var pt = new Vec2(x, y);
        var uvw = pt.coordsInTriangle(a, b, c);
        if (uvw.allNonNegative()) {
            var c1 = color1.getColor(x, y).multiply(uvw.x);
            var c2 = color2.getColor(x, y).multiply(uvw.y);
            var c3 = color3.getColor(x, y).multiply(uvw.z);
            return c1.add(c2).add(c3);
        }
        return background.getColor(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var a = new Vec2(x1.getDouble(x, y), y1.getDouble(x, y));
        var b = new Vec2(x2.getDouble(x, y), y2.getDouble(x, y));
        var c = new Vec2(x3.getDouble(x, y), y3.getDouble(x, y));
        var pt = new Vec2(x, y);
        var uvw = pt.coordsInTriangle(a, b, c);
        if (uvw.allNonNegative()) {
            var c1 = color1.getDouble(x, y) * uvw.x;
            var c2 = color2.getDouble(x, y) * uvw.y;
            var c3 = color3.getDouble(x, y) * uvw.z;
            return c1 + c2 + c3;
        }
        return background.getDouble(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        background.init(outputSize, randomize);
        x1.init(outputSize, randomize);
        y1.init(outputSize, randomize);
        color1.init(outputSize, randomize);
        x2.init(outputSize, randomize);
        y2.init(outputSize, randomize);
        color2.init(outputSize, randomize);
        x3.init(outputSize, randomize);
        y3.init(outputSize, randomize);
        color3.init(outputSize, randomize);
    }
}
