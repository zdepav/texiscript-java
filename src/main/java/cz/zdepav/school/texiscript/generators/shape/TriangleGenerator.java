package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Vec2;

/** @author Zdenek Pavlatka */
public class TriangleGenerator extends ShapeGenerator {

    private final Generator x1, y1, x2, y2, x3, y3;

    public TriangleGenerator(
        CodePosition pos,
        Generator color, Generator background,
        Generator x1, Generator y1,
        Generator x2, Generator y2,
        Generator x3, Generator y3
    ) {
        super(color, background);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    @Override
    protected boolean isInside(double x, double y) {
        var a = new Vec2(x1.getDouble(x, y), y1.getDouble(x, y));
        var v0 = new Vec2(x3.getDouble(x, y), y3.getDouble(x, y)).sub(a);
        var v1 = new Vec2(x2.getDouble(x, y), y2.getDouble(x, y)).sub(a);
        var v2 = new Vec2(x, y).sub(a);

        var dot00 = v0.dot(v0);
        var dot01 = v0.dot(v1);
        var dot02 = v0.dot(v2);
        var dot11 = v1.dot(v1);
        var dot12 = v1.dot(v2);

        var invDenom = 1 / (dot00 * dot11 - dot01 * dot01);
        var u = (dot11 * dot02 - dot01 * dot12) * invDenom;
        var v = (dot00 * dot12 - dot01 * dot02) * invDenom;

        return (u >= 0) && (v >= 0) && (u + v < 1);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x1.init(outputSize, randomize);
        y1.init(outputSize, randomize);
        x2.init(outputSize, randomize);
        y2.init(outputSize, randomize);
        x3.init(outputSize, randomize);
        y3.init(outputSize, randomize);
    }
}
