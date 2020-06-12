package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Base class for perlin-based generators. */
public abstract class PerlinGenerator extends Generator {

    @FunctionalInterface
    public interface BasicConstructor {
        Generator construct(Generator color1, Generator color2, double scale, Generator curve);
    }

    /** whether generation should be fully randomized or deterministic */
    protected boolean randomized;

    /** grid color */
    protected final Generator color1;

    /** fill color */
    protected final Generator color2;

    /** curve for color interpolation */
    protected final Generator curve;

    protected double inverseScale;

    protected PerlinGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        this.color1 = color1;
        this.color2 = color2;
        inverseScale = 1 / (Utils.clamp(scale, 0.0625, 16) * 32);
        this.curve = curve;
        randomized = false;
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        color1.init(outputSize, randomize);
        color2.init(outputSize, randomize);
        curve.init(outputSize, randomize);
    }

    protected abstract double get(double x, double y);

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return color1.getColor(x, y).lerp(
            color2.getColor(x, y),
            curve.getDouble(get(x * 256, y * 256) / 2 + 0.5, y)
        );
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return Utils.lerp(
            color1.getDouble(x, y),
            color2.getDouble(x, y),
            curve.getDouble(get(x * 256, y * 256) / 2 + 0.5, y)
        );
    }

    /**
     * Builds the generator.
     * @param pos current position in script
     * @param args function arguments
     * @param name generator name
     * @param constructor generator constructor to use
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(
        CodePosition pos,
        Generator[] args,
        String name,
        BasicConstructor constructor
    ) throws SemanticException {
        if (args.length == 0) {
            return constructor.construct(
                Generator.get(0),
                Generator.get(1),
                1, CurveGenerator.LINEAR
            );
        } else if (args[0].isNumber()) {
            switch (args.length) {
                case 1:
                    return constructor.construct(
                        Generator.get(0),
                        Generator.get(1),
                        args[0].getDouble(0, 0),
                        CurveGenerator.LINEAR
                    );
                case 2:
                    return constructor.construct(
                        Generator.get(0),
                        args[1],
                        args[0].getDouble(0, 0),
                        CurveGenerator.LINEAR
                    );
                case 3:
                    return constructor.construct(
                        args[1], args[2],
                        args[0].getDouble(0, 0),
                        CurveGenerator.LINEAR
                    );
                case 4:
                    return constructor.construct(args[1], args[2], args[0].getDouble(0, 0), args[3]);
                default:
                    throw new SemanticException(pos, "perlin." + name + " requires 0 to 4 arguments");
            }
        } else {
            throw new SemanticException(pos, "perlin." + name + " expects a number as its first argument");
        }
    }
}
