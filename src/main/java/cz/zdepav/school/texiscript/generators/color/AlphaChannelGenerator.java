package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class AlphaChannelGenerator extends Generator {

    private final Generator base;

    public AlphaChannelGenerator(CodePosition pos, Generator base) { this.base = base; }

    @Override
    public RgbaColor getColor(double x, double y) {
        var col = base.getColor(x, y);
        return new RgbaColor(col.a);
    }

    @Override
    public double getDouble(double x, double y) { return base.getColor(x, y).a; }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        var col = base.getColor(x, y);
        return Utils.lerp(min, max, col.a);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
