package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class GrayscaleGenerator extends Generator {

    private final Generator base;

    public GrayscaleGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var col = base.getColor(x, y);
        return new RgbaColor(col.intensity, col.intensity, col.intensity, col.a);
    }

    @Override
    public double getDouble(double x, double y) {
        return base.getDouble(x, y);
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return base.getDouble(x, y, min, max);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
