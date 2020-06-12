package cz.zdepav.school.texiscript.generators.curve;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Generates cubic bezier curve y value based on x coordinate. */
public class CubicBezierCurveGenerator extends Generator {

    /** horizontal coordinate of the first control point */
    private final Generator c1x;

    /** vertical coordinate of the first control point */
    private final Generator c1y;

    /** horizontal coordinate of the second control point */
    private final Generator c2x;

    /** vertical coordinate of the second control point */
    private final Generator c2y;

    /** curve starting point (on horizontal axis) */
    private final Generator from;

    /** curve ending point (on horizontal axis) */
    private final Generator to;

    /** curve minimum value */
    private final Generator min;

    /** curve maximum value */
    private final Generator max;

    public CubicBezierCurveGenerator(Generator c1x, Generator c1y, Generator c2x, Generator c2y) {
        this.c1x = c1x;
        this.c1y = c1y;
        this.c2x = c2x;
        this.c2y = c2y;
        this.from = this.min = Generator.get(0);
        this.to = this.max = Generator.get(1);
    }

    public CubicBezierCurveGenerator(
        Generator c1x, Generator c1y,
        Generator c2x, Generator c2y,
        Generator from, Generator to
    ) {
        this.c1x = c1x;
        this.c1y = c1y;
        this.c2x = c2x;
        this.c2y = c2y;
        this.from = from;
        this.to = to;
        this.min = Generator.get(0);
        this.max = Generator.get(1);
    }

    public CubicBezierCurveGenerator(
        Generator c1x, Generator c1y,
        Generator c2x, Generator c2y,
        Generator from, Generator to,
        Generator min, Generator max
    ) {
        this.c1x = c1x;
        this.c1y = c1y;
        this.c2x = c2x;
        this.c2y = c2y;
        this.from = from;
        this.to = to;
        this.min = min;
        this.max = max;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(getDouble(x, y));
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var from = this.from.getDouble(x, y);
        var to = this.to.getDouble(x, y);
        var min = this.min.getDouble(x, y);
        var max = this.max.getDouble(x, y);
        var c1x = Utils.clamp(this.c1x.getDouble(x, y), 0, 1);
        var c1y = Utils.clamp(this.c1y.getDouble(x, y), 0, 1);
        var c2x = Utils.clamp(this.c2x.getDouble(x, y), 0, 1);
        var c2y = Utils.clamp(this.c2y.getDouble(x, y), 0, 1);
        var A = 1 - 3 * c2x + 3 * c1x;
        var B = 3 * c2x - 6 * c1x;
        var C = 3 * c1x;
        var E = 1 - 3 * c2y + 3 * c1y;
        var F = 3 * c2y - 6 * c1y;
        var G = 3 * c1y;
        double f;
        if (from < to) {
            f = (x - from) / (to - from);
        } else if (to < from) {
            f = (from - x) / (from - to);
        } else /* from == to */ {
            f = x < from ? 0 : 1;
        }
        var t = f;
        var nRefinementIterations = 5;
        for (var i = 0; i < nRefinementIterations; i++) {
            t = Utils.clamp(
                t - ((A * t * t + B * t + C) * t - f) / (3.0 * A * t * t + 2.0 * B * t + C),
                0,
                1
            );
        }
        return Utils.lerp(min, max, (E * t * t + F * t + G) * t);
    }

    /**
     * Builds a curve generator with given curve.
     * @param pos current position in script
     * @param args function arguments
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 4:
                return new CubicBezierCurveGenerator(args[0], args[1], args[2], args[3]);
            case 6:
                return new CubicBezierCurveGenerator(args[0], args[1], args[2], args[3], args[4], args[5]);
            case 8:
                return new CubicBezierCurveGenerator(
                    args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]
                );
            default:
                throw new SemanticException(pos, "curve.bezier requires 4, 6 or 8 arguments");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        c1x.init(outputSize, randomize);
        c1y.init(outputSize, randomize);
        c2x.init(outputSize, randomize);
        c2y.init(outputSize, randomize);
        from.init(outputSize, randomize);
        to.init(outputSize, randomize);
        min.init(outputSize, randomize);
        max.init(outputSize, randomize);
    }
}
