package cz.zdepav.school.texiscript.generators.gradients;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** Generates radial gradient around a point between two radii. */
public class RadialGradientGenerator extends GradientGenerator {

    private final Generator x, y, r1, r2;

    public RadialGradientGenerator(Generator x, Generator y, Generator r1, Generator r2) {
        this.x = x;
        this.y = y;
        this.r1 = r1;
        this.r2 = r2;
    }

    /** {@inheritDoc} */
    @Override
    protected double getPosition(double x, double y) {
        var r1 = this.r1.getDouble(x, y);
        var dx = x - this.x.getDouble(x, y);
        var dy = y - this.y.getDouble(x, y);
        return (Math.sqrt(dx * dx + dy * dy) - r1) / (this.r2.getDouble(x, y) - r1);
    }

    /**
     * Builds the generator.
     * @param pos current position in script
     * @param args function arguments
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args.length < 6) {
            throw new SemanticException(pos, "gradient.radial requires at least 6 arguments");
        }
        var g = new RadialGradientGenerator(args[0], args[1], args[2], args[3]);
        for (int i = 4; i < args.length; ++i) {
            if (args[i] instanceof GradientStopGenerator) {
                g.addColorStop((GradientStopGenerator)args[i]);
            } else {
                throw new SemanticException(pos, "Argument " + (i + 1) + " of gradient.radial must be gradient.stop");
            }
        }
        return g;
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
}
