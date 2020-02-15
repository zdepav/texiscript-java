package cz.zdepav.school.texiscript.generators.mix;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class SwitchGenerator extends Generator {

    private final Generator[] generators;
    private final Generator value;

    public SwitchGenerator(Generator value, Generator[] generators) {
        this.value = value;
        this.generators = generators;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var last = generators.length - 1;
        var val = value.getDouble(x, y) * last;
        var i = (int)val;
        if (val <= 0) {
            return generators[0].getColor(x, y);
        } else if (i >= last) {
            return generators[last].getColor(x, y);
        } else {
            return generators[i].getColor(x, y).lerp(generators[i + 1].getColor(x, y), val % 1);
        }
    }

    @Override
    public double getDouble(double x, double y) {
        var last = generators.length - 1;
        var val = value.getDouble(x, y) * last;
        var i = (int)val;
        if (val <= 0) {
            return generators[0].getDouble(x, y);
        } else if (i >= last) {
            return generators[last].getDouble(x, y);
        } else {
            return Utils.lerp(generators[i].getDouble(x, y), generators[i + 1].getDouble(x, y), val % 1);
        }
    }

    public boolean isNumber() {
        for (var gen: generators) {
            if (!gen.isNumber()) {
                return false;
            }
        }
        return value.isNumber();
    }

    public boolean isColor() {
        for (var gen: generators) {
            if (!gen.isColor()) {
                return false;
            }
        }
        return value.isNumber();
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args.length < 3) {
            throw new SemanticException(pos, "mix.choice requires at least 3 arguments");
        }
        var gens = new Generator[args.length - 1];
        System.arraycopy(args, 1, gens, 0, gens.length);
        return new SwitchGenerator(args[0], gens);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        value.init(outputSize, randomize);
        for (var gen: generators) {
            gen.init(outputSize, randomize);
        }
    }
}
