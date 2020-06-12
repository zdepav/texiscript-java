package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Extracts saturation from its input. */
public class SaturationGenerator extends Generator {

    /** input generator */
    private final Generator base;

    public SaturationGenerator(CodePosition pos, Generator base) { this.base = base; }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(base.getColor(x, y).saturation());
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) { return base.getColor(x, y).saturation(); }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, base.getColor(x, y).saturation());
    }

    /** {@inheritDoc} */
    @Override
    public boolean isNumber() {
        return base.isColor();
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
