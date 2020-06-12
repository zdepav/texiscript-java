package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Metric;

/** Generates an ellipse. */
public class EllipseGenerator extends ShapeGenerator {

    private final Generator x, y, r1, r2;

    public EllipseGenerator(
        Generator color, Generator background,
        Generator x, Generator y,
        Generator r1, Generator r2,
        Generator bevel, Generator curve
    ) {
        super(color, background, bevel, curve);
        this.x = x;
        this.y = y;
        this.r1 = r1;
        this.r2 = r2;
    }

    /** {@inheritDoc} */
    @Override
    protected double distanceFromCenter(double x, double y) {
        return Metric.euclidean.distance(
            (x - this.x.getDouble(x, y)) / r1.getDouble(x, y),
            (y - this.y.getDouble(x, y)) / r2.getDouble(x, y)
        );
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        r1.init(outputSize, randomize);
        r2.init(outputSize, randomize);
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
        if (args.length < 6 || args.length > 8) {
            throw new SemanticException(pos, "shape.ellipse requires 6 to 8 arguments");
        }
        var bevel = args.length > 6 ? args[6] : Generator.get(0);
        var curve = args.length == 8 ? args[7] : CurveGenerator.LINEAR;
        return new EllipseGenerator(args[0], args[1], args[2], args[3], args[4], args[5], bevel, curve);
    }
}
