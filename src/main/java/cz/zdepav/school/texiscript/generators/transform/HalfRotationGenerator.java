package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Rotates its input 180 degrees. */
public class HalfRotationGenerator extends Generator {

    private final Generator base;

    public HalfRotationGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(1 - x, 1 - y);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return base.getDouble(1 - x, 1 - y);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
