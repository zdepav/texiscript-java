package cz.zdepav.school.texiscript.generators.math;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class BatchMultiplicationGenerator extends Generator {

    private final Generator[] gens;

    private BatchMultiplicationGenerator(Generator[] gens) {
        this.gens = gens;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var c = gens[0].getColor(x, y);
        for (var i = 1; i < gens.length; ++i) {
            c = c.multiply(gens[i].getColor(x, y));
        }
        return c;
    }

    @Override
    public double getDouble(double x, double y) {
        var n = gens[0].getDouble(x, y);
        for (var i = 1; i < gens.length; ++i) {
            n *= gens[i].getDouble(x, y);
        }
        return n;
    }

    @Override
    public boolean isNumber() {
        for (var g: gens) {
            if (!g.isNumber()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isColor() {
        for (var g: gens) {
            if (!g.isColor()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        for (var g: gens) {
            g.init(outputSize, randomize);
        }
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args == null || args.length < 2) {
            throw new SemanticException(pos, "math.mul requires at least 2 arguments");
        }
        if (args.length == 2) {
            return new MultiplicationGenerator(args[0], args[1]);
        } else {
            return new BatchMultiplicationGenerator(args);
        }
    }
}
