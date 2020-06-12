package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Rotates its input 90 degrees clockwise. */
public class RightRotationGenerator extends Generator {

    private final Generator base;

    public RightRotationGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(y, 1 - x);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return base.getDouble(y, 1 - x);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
