package cz.zdepav.school.texiscript.generators.mix;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class QuattroGenerator extends Generator {

    private final Generator g00, g10, g01, g11;

    public QuattroGenerator(CodePosition pos, Generator g00, Generator g10, Generator g01, Generator g11) {
        this.g00 = g00;
        this.g10 = g10;
        this.g01 = g01;
        this.g11 = g11;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        x *= 2;
        y *= 2;
        if (y < 1) {
            return x < 1 ? g00.getColor(x, y) : g10.getColor(x - 1, y);
        } else {
            return x < 1 ? g01.getColor(x, y - 1) : g11.getColor(x - 1, y - 1);
        }
    }

    @Override
    public double getDouble(double x, double y) {
        x *= 2;
        y *= 2;
        if (y < 1) {
            return x < 1 ? g00.getDouble(x, y) : g10.getDouble(x - 1, y);
        } else {
            return x < 1 ? g01.getDouble(x, y - 1) : g11.getDouble(x - 1, y - 1);
        }
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        g00.init(outputSize, randomize);
        g01.init(outputSize, randomize);
        g10.init(outputSize, randomize);
        g11.init(outputSize, randomize);
    }
}
