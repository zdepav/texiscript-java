package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Generates pseudo-random value. */
public class RandomGenerator extends Generator {

    /** input generator */
    private final Generator base;

    public RandomGenerator(Generator base) {
        this.base = base;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var i = getRandom(base.getDouble(x, y), 0.5).r();
        return new RgbaColor(i);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return getRandom(base.getDouble(x, y), 0.5).r();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return base.isNumber();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isColor() {
        return base.isColor();
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }

    /**
     * Builds the generator.
     * @param pos current position in script
     * @param args function arguments
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args == null || args.length == 0) {
            return new RandomGenerator(Generator.get(0));
        } else if (args.length == 1) {
            return new RandomGenerator(args[0]);
        } else {
            throw new SemanticException(pos, "math.random requires at most 1 argument");
        }
    }
}
