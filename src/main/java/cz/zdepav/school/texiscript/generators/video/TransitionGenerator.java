package cz.zdepav.school.texiscript.generators.video;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.cellular.MosaicGenerator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Metric;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Interpolates between inputs based on the position in video. */
public class TransitionGenerator extends Generator {

    private final Generator min, max, curve;

    public TransitionGenerator(Generator min, Generator max, Generator curve) {
        this.min = min;
        this.max = max;
        this.curve = curve;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return min.getColor(x, y).lerp(max.getColor(x, y), curve.getDouble(TimeGenerator.getGlobalTime(), y));
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return Utils.lerp(min.getDouble(x, y), max.getDouble(x, y), curve.getDouble(TimeGenerator.getGlobalTime(), y));
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        min.init(outputSize, randomize);
        max.init(outputSize, randomize);
        curve.init(outputSize, randomize);
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 2:
                return new TransitionGenerator(args[0], args[1], CurveGenerator.LINEAR);
            case 3:
                return new TransitionGenerator(args[0], args[1], args[2]);
            default:
                throw new SemanticException(pos, "video.transition requires 2 or 3 arguments");
        }
    }
}
