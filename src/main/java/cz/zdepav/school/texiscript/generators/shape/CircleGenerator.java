package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Metric;

/** Generates a circle. */
public class CircleGenerator extends ShapeGenerator {

    private final Generator x, y, r;

    public CircleGenerator(
        Generator color, Generator background,
        Generator x, Generator y, Generator r,
        Generator bevel, Generator curve
    ) {
        super(color, background, bevel, curve);
        this.x = x;
        this.y = y;
        this.r = r;
    }

    /** {@inheritDoc} */
    @Override
    protected double distanceFromCenter(double x, double y) {
        return Metric.euclidean.distance(
            x - this.x.getDouble(x, y),
            y - this.y.getDouble(x, y)
        ) / r.getDouble(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        r.init(outputSize, randomize);
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
        if (args.length < 5 || args.length > 7) {
            throw new SemanticException(pos, "shape.circle requires 5 to 7 arguments");
        }
        var bevel = args.length > 5 ? args[5] : Generator.get(0);
        var curve = args.length == 7 ? args[6] : CurveGenerator.LINEAR;
        return new CircleGenerator(args[0], args[1], args[2], args[3], args[4], bevel, curve);
    }
}
