package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Generates a triangle. */
public class TriangleGenerator extends ShapeGenerator {

    private final Generator x1, y1, x2, y2, x3, y3;

    public TriangleGenerator(
        Generator color, Generator background,
        Generator x1, Generator y1,
        Generator x2, Generator y2,
        Generator x3, Generator y3,
        Generator bevel, Generator curve
    ) {
        super(color, background, bevel, curve);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
    }

    /** {@inheritDoc} */
    @Override
    protected double distanceFromCenter(double x, double y) {
        var a = new Vec2(x1.getDouble(x, y), y1.getDouble(x, y));
        var b = new Vec2(x2.getDouble(x, y), y2.getDouble(x, y));
        var c = new Vec2(x3.getDouble(x, y), y3.getDouble(x, y));
        var center = a.add(b).add(c).mul(0.33333);
        var pt = new Vec2(x, y);
        var uvw = pt.coordsInTriangle(center, b, c);
        if (uvw.allNonNegative()) {
            return 1 - uvw.x;
        }
        uvw = pt.coordsInTriangle(center, a, c);
        if (uvw.allNonNegative()) {
            return 1 - uvw.x;
        }
        uvw = pt.coordsInTriangle(center, a, b);
        if (uvw.allNonNegative()) {
            return 1 - uvw.x;
        }
        uvw = pt.coordsInTriangle(a, b, c);
        if (uvw.allNonNegative()) {
            return (Math.max(uvw.x, Math.max(uvw.y, uvw.z)) - 0.33333) * 1.5;
        }
        return 2.0;
    }

    /** {@inheritDoc} */
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

    /**
     * Builds the generator.
     * @param pos current position in script
     * @param args function arguments
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(
        CodePosition pos,
        Generator[] args
    ) throws SemanticException {
        if (args.length < 8 || args.length > 10) {
            throw new SemanticException(pos, "shape.triangle requires 8 to 10 arguments");
        }
        var bevel = args.length > 8 ? args[8] : Generator.get(0);
        var curve = args.length == 10 ? args[9] : CurveGenerator.LINEAR;
        return new TriangleGenerator(
            args[0], args[1],
            args[2], args[3],
            args[4], args[5],
            args[6], args[7],
            bevel, curve
        );
    }
}
