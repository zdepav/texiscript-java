package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** Generates a rectangle. */
public class RectangleGenerator extends ShapeGenerator {

    private final Generator x, y, width, height;

    public RectangleGenerator(
        Generator color, Generator background,
        Generator x, Generator y,
        Generator width, Generator height,
        Generator bevel, Generator curve
    ) {
        super(color, background, bevel, curve);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /** {@inheritDoc} */
    @Override
    protected double distanceFromCenter(double x, double y) {
        x -= this.x.getDouble(x, y);
        y -= this.y.getDouble(x, y);
        var w = width.getDouble(x, y);
        var h = height.getDouble(x, y);
        x = w > 0 ? x / w : x * 1_000_000.0;
        y = h > 0 ? y / h : y * 1_000_000.0;
        if (x < y) {
            return x + y < 1 ? (0.5 - x) * 2 : (y - 0.5) * 2;
        } else {
            return x + y < 1 ? (0.5 - y) * 2 : (x - 0.5) * 2;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        width.init(outputSize, randomize);
        height.init(outputSize, randomize);
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
            throw new SemanticException(pos, "shape.rect requires 6 to 8 arguments");
        }
        var bevel = args.length > 6 ? args[6] : Generator.get(0);
        var curve = args.length == 8 ? args[7] : CurveGenerator.LINEAR;
        return new RectangleGenerator(args[0], args[1], args[2], args[3], args[4], args[5], bevel, curve);
    }
}
