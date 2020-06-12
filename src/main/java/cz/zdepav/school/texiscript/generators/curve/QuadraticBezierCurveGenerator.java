package cz.zdepav.school.texiscript.generators.curve;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Generates quadratic bezier curve y value based on x coordinate. */
public class QuadraticBezierCurveGenerator extends Generator {

    /** horizontal coordinate of the control point */
    private final Generator cx;

    /** vertical coordinate of the control point */
    private final Generator cy;

    /** curve starting point (on horizontal axis) */
    private final Generator from;

    /** curve ending point (on horizontal axis) */
    private final Generator to;

    /** curve minimum value */
    private final Generator min;

    /** curve maximum value */
    private final Generator max;

    public QuadraticBezierCurveGenerator(Generator cx, Generator cy) {
        this.cx = cx;
        this.cy = cy;
        this.from = this.min = Generator.get(0);
        this.to = this.max = Generator.get(1);
    }

    public QuadraticBezierCurveGenerator(Generator cx, Generator cy, Generator from, Generator to) {
        this.cx = cx;
        this.cy = cy;
        this.from = from;
        this.to = to;
        this.min = Generator.get(0);
        this.max = Generator.get(1);
    }

    public QuadraticBezierCurveGenerator(
        Generator cx, Generator cy,
        Generator from, Generator to,
        Generator min, Generator max
    ) {
        this.cx = cx;
        this.cy = cy;
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
        var cx = Utils.clamp(this.cx.getDouble(x, y), 0, 1);
        var cy = Utils.clamp(this.cy.getDouble(x, y), 0, 1);
        double f;
        if (from < to) {
            f = (x - from) / (to - from);
        } else if (to < from) {
            f = (from - x) / (from - to);
        } else /* from == to */ {
            f = x < from ? 0 : 1;
        }
        if (cx == 0.5){
            cx = 0.50001;
        }
        var om2cx = 1 - 2 * cx;
        var t = (Math.sqrt(cx * cx + om2cx * Utils.clamp(f, 0, 1)) - cx) / om2cx;
        return Utils.lerp(min, max, (t + 2 * cy * (1 - t)) * t);
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
            case 2:
                return new QuadraticBezierCurveGenerator(args[0], args[1]);
            case 4:
                return new QuadraticBezierCurveGenerator(args[0], args[1], args[2], args[3]);
            case 6:
                return new QuadraticBezierCurveGenerator(args[0], args[1], args[2], args[3], args[4], args[5]);
            default:
                throw new SemanticException(pos, "curve.quadratic requires 2, 4 or 6 arguments");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        cx.init(outputSize, randomize);
        cy.init(outputSize, randomize);
        from.init(outputSize, randomize);
        to.init(outputSize, randomize);
        min.init(outputSize, randomize);
        max.init(outputSize, randomize);
    }
}
