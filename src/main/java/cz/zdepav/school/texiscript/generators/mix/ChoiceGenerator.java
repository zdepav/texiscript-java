package cz.zdepav.school.texiscript.generators.mix;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Chooses between its inputs based on a value. */
public class ChoiceGenerator extends Generator {

    private final Generator[] generators;

    private final Generator value;

    private ChoiceGenerator(Generator value, Generator[] generators) {
        this.value = value;
        this.generators = generators;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return generators[Utils.lerpInt(0, generators.length - 1, value.getDouble(x, y))].getColor(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return generators[Utils.lerpInt(0, generators.length - 1, value.getDouble(x, y))].getDouble(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        for (var gen: generators) {
            if (!gen.isNumber()) {
                return false;
            }
        }
        return value.isNumber();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isColor() {
        for (var gen: generators) {
            if (!gen.isColor()) {
                return false;
            }
        }
        return value.isNumber();
    }

    /**
     * Builds the generator.
     * @param pos current position in script
     * @param args function arguments
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args.length < 3) {
            throw new SemanticException(pos, "mix.choice requires at least 3 arguments");
        }
        var gens = new Generator[args.length - 1];
        System.arraycopy(args, 1, gens, 0, gens.length);
        return new ChoiceGenerator(args[0], gens);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        value.init(outputSize, randomize);
        for (var gen: generators) {
            gen.init(outputSize, randomize);
        }
    }
}
