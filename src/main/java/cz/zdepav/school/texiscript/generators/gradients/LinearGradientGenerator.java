package cz.zdepav.school.texiscript.generators.gradients;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class LinearGradientGenerator extends GradientGenerator {

    private final Generator x1, y1, x2, y2;

    public LinearGradientGenerator(Generator x1, Generator y1, Generator x2, Generator y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    protected double getPosition(double x, double y) {
        var a = x2.getDouble(x, y) - x1.getDouble(x, y);
        var b = y2.getDouble(x, y) - y1.getDouble(x, y);
        var c = -a * x1.getDouble(x, y) - b * y1.getDouble(x, y);
        return (a * x + b * y + c) / (a * a + b * b);
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args.length < 6) {
            throw new SemanticException(pos, "gradient.linear requires at least 6 arguments");
        }
        var g = new LinearGradientGenerator(args[0], args[1], args[2], args[3]);
        for (int i = 4; i < args.length; ++i) {
            if (args[i] instanceof GradientStopGenerator) {
                g.addColorStop((GradientStopGenerator)args[i]);
            } else {
                throw new SemanticException(pos, "Argument " + (i + 1) + " of gradient.linear must be gradient.stop");
            }
        }
        return g;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x1.init(outputSize, randomize);
        y1.init(outputSize, randomize);
        x2.init(outputSize, randomize);
        y2.init(outputSize, randomize);
    }
}
