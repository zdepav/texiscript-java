package cz.zdepav.school.texiscript.generators.gradients;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Angle;
import cz.zdepav.school.texiscript.utils.Utils;
import cz.zdepav.school.texiscript.utils.Vec2;

/** @author Zdenek Pavlatka */
public class AngularGradientGenerator extends GradientGenerator {

    private Generator x, y;

    public AngularGradientGenerator(Generator x, Generator y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected double getPosition(double x, double y) {
        var v = new Vec2(this.x.getDouble(x, y), this.y.getDouble(x, y));
        return Utils.wrap(v.directionTo(x, y) / Angle.deg360);
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args.length < 4) {
            throw new SemanticException(pos, "gradient.angular requires at least 4 arguments");
        }
        var g = new AngularGradientGenerator(args[0], args[1]);
        for (int i = 2; i < args.length; ++i) {
            if (args[i] instanceof GradientStopGenerator) {
                g.addColorStop((GradientStopGenerator)args[i]);
            } else {
                throw new SemanticException(pos, "Argument " + (i + 1) + " of gradient.angular must be gradient.stop");
            }
        }
        return g;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
    }
}
