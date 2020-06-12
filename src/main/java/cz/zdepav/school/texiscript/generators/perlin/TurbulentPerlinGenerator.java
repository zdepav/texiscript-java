package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Base class for perlin-based generators with turbulence. */
public abstract class TurbulentPerlinGenerator extends PerlinGenerator {

    @FunctionalInterface
    public interface TurbulentConstructor {
        Generator construct(Generator color1, Generator color2, double scale, Generator turbulence, Generator curve);
    }

    protected final Generator turbulence;

    public TurbulentPerlinGenerator(
        Generator color1, Generator color2,
        double scale, Generator turbulence,
        Generator curve
    ) {
        super(color1, color2, scale, curve);
        this.turbulence = turbulence;
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
        TurbulentConstructor constructor
    ) throws SemanticException {
        if (args.length == 0) {
            return constructor.construct(
                Generator.get(0),
                Generator.get(1),
                1, Generator.get(1),
                CurveGenerator.LINEAR
            );
        } else if (args[0].isNumber()) {
            if (args.length == 1) {
                return constructor.construct(
                    Generator.get(0),
                    Generator.get(1),
                    args[0].getDouble(0, 0),
                    Generator.get(1),
                    CurveGenerator.LINEAR
                );
            } else {
                switch (args.length) {
                    case 2:
                        return constructor.construct(
                            Generator.get(0),
                            Generator.get(1),
                            args[0].getDouble(0, 0),
                            args[1], CurveGenerator.LINEAR
                        );
                    case 3:
                        return constructor.construct(
                            Generator.get(0),
                            args[2],
                            args[0].getDouble(0, 0),
                            args[1], CurveGenerator.LINEAR
                        );
                    case 4:
                        return constructor.construct(
                            args[2], args[3],
                            args[0].getDouble(0, 0),
                            args[1], CurveGenerator.LINEAR
                        );
                    case 5:
                        return constructor.construct(
                            args[2], args[3],
                            args[0].getDouble(0, 0),
                            args[1], args[4]
                        );
                    default:
                        throw new SemanticException(pos, "perlin." + name + " requires 0 to 5 arguments");
                }
            }
        } else {
            throw new SemanticException(pos, "perlin." + name + " expects a number as its first argument");
        }
    }
}
