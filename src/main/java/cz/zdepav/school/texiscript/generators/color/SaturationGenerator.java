package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class SaturationGenerator extends Generator {

    private final Generator base;

    public SaturationGenerator(CodePosition pos, Generator base) { this.base = base; }

    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(base.getColor(x, y).saturation());
    }

    @Override
    public double getDouble(double x, double y) { return base.getColor(x, y).saturation(); }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, base.getColor(x, y).saturation());
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}