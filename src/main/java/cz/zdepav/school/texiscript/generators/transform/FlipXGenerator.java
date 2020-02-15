package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class FlipXGenerator extends Generator {

    private final Generator base;

    public FlipXGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(1 - x, y);
    }

    @Override
    public double getDouble(double x, double y) {
        return base.getDouble(1 - x, y);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
