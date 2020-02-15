package cz.zdepav.school.texiscript.generators.mix;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class SidesGenerator extends Generator {

    private final Generator top, right, bottom, left;

    public SidesGenerator(CodePosition pos, Generator top, Generator right, Generator bottom, Generator left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        if (x + y < 1) {
            return x < y ? left.getColor(x, y) : top.getColor(x, y);
        } else {
            return x < y ? bottom.getColor(x, y) : right.getColor(x, y);
        }
    }

    @Override
    public double getDouble(double x, double y) {
        if (x + y < 1) {
            return x < y ? left.getDouble(x, y) : top.getDouble(x, y);
        } else {
            return x < y ? bottom.getDouble(x, y) : right.getDouble(x, y);
        }
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        top.init(outputSize, randomize);
        right.init(outputSize, randomize);
        bottom.init(outputSize, randomize);
        left.init(outputSize, randomize);
    }
}
