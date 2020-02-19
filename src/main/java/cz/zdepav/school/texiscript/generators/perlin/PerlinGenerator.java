package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public abstract class PerlinGenerator extends Generator {

    @FunctionalInterface
    public interface BasicConstructor {
        Generator construct(Generator color1, Generator color2, double scale, Generator curve);
    }

    protected boolean randomized;
    protected Generator color1, color2, curve;
    protected double inverseScale;

    protected PerlinGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        this.color1 = color1;
        this.color2 = color2;
        inverseScale = 1 / (Utils.clamp(scale, 0.0625, 16) * 32);
        this.curve = curve;
        randomized = false;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        color1.init(outputSize, randomize);
        color2.init(outputSize, randomize);
        curve.init(outputSize, randomize);
    }

    protected abstract double get(double x, double y);

    @Override
    public RgbaColor getColor(double x, double y) {
        return color1.getColor(x, y).lerp(
            color2.getColor(x, y),
            curve.getDouble(get(x * 256, y * 256) / 2 + 0.5, y)
        );
    }

    @Override
    public double getDouble(double x, double y) {
        return Utils.lerp(
            color1.getDouble(x, y),
            color2.getDouble(x, y),
            curve.getDouble(get(x * 256, y * 256) / 2 + 0.5, y)
        );
    }

    public static Generator build(
        CodePosition pos,
        Generator[] args,
        String name,
        BasicConstructor constructor
    ) throws SemanticException {
        if (args.length == 0) {
            return constructor.construct(
                RgbaColor.black.generator(),
                RgbaColor.white.generator(),
                1, CurveGenerator.LINEAR
            );
        } else if (args[0].isNumber()) {
            switch (args.length) {
                case 1:
                    return constructor.construct(
                        RgbaColor.black.generator(),
                        RgbaColor.white.generator(),
                        args[0].getDouble(0, 0),
                        CurveGenerator.LINEAR
                    );
                case 2:
                    return constructor.construct(
                        RgbaColor.black.generator(),
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
