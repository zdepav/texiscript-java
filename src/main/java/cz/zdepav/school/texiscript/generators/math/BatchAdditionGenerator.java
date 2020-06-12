package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Adds multiple values. */
public class BatchAdditionGenerator extends Generator {

    private final Generator[] gens;

    private BatchAdditionGenerator(Generator[] gens) {
        this.gens = gens;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var c = gens[0].getColor(x, y);
        for (var i = 1; i < gens.length; ++i) {
            c = c.add(gens[i].getColor(x, y));
        }
        return c;
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var n = gens[0].getDouble(x, y);
        for (var i = 1; i < gens.length; ++i) {
            n += gens[i].getDouble(x, y);
        }
        return n;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        for (var g: gens) {
            if (!g.isNumber()) {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isColor() {
        for (var g: gens) {
            if (!g.isColor()) {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        for (var g: gens) {
            g.init(outputSize, randomize);
        }
    }

    /**
     * Builds the generator.
     * @param pos current position in script
     * @param args function arguments
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args == null || args.length < 2) {
            throw new SemanticException(pos, "math.add requires at least 2 arguments");
        }
        if (args.length == 2) {
            return new AdditionGenerator(args[0], args[1]);
        } else {
            return new BatchAdditionGenerator(args);
        }
    }
}
