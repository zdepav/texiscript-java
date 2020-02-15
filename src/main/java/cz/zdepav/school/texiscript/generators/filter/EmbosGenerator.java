package cz.zdepav.school.texiscript.generators.filter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.NumberGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class EmbosGenerator extends Generator {

    private static double offset = 0.0001;

    private final Generator base, mask, strength;

    private EmbosGenerator(Generator base, Generator mask, Generator strength) {
        this.base = base;
        this.mask = mask;
        this.strength = strength;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var a = mask.getDouble(x - offset, y - offset);
        var b = mask.getDouble(x + offset, y + offset);
        return base.getColor(x, y).add((b - a) * 20 * strength.getDouble(x, y));
    }

    @Override
    public double getDouble(double x, double y) {
        var a = mask.getDouble(x - offset, y - offset);
        var b = mask.getDouble(x + offset, y + offset);
        return base.getDouble(x, y) + (b - a) * 20 * strength.getDouble(x, y);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
        mask.init(outputSize, randomize);
        strength.init(outputSize, randomize);
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 2:
                return new EmbosGenerator(args[0], args[1], Generator.get(1));
            case 3:
                return new EmbosGenerator(args[0], args[1], args[2]);
            default:
                throw new SemanticException(pos, "filter.emboss requires 2 or 3 arguments");
        }
    }
}
