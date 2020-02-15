package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class AtGenerator extends Generator {

    private final Generator base, x, y;

    public AtGenerator(CodePosition pos, Generator base, Generator x, Generator y) {
        this.base = base;
        this.x = x;
        this.y = y;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(this.x.getDouble(x, y), this.y.getDouble(x, y));
    }

    @Override
    public double getDouble(double x, double y) {
        return base.getDouble(this.x.getDouble(x, y), this.y.getDouble(x, y));
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
    }
}
