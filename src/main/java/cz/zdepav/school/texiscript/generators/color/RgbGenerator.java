package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Converts RGB to a color. */
public class RgbGenerator extends Generator {

    /** red input generator */
    private final Generator rGen;

    /** green input generator */
    private final Generator gGen;

    /** blue input generator */
    private final Generator bGen;

    public RgbGenerator(CodePosition pos, Generator rGen, Generator gGen, Generator bGen) {
        this.rGen = rGen;
        this.gGen = gGen;
        this.bGen = bGen;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(rGen.getDouble(x, y), gGen.getDouble(x, y), bGen.getDouble(x, y));
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return (rGen.getDouble(x, y) + gGen.getDouble(x, y) + bGen.getDouble(x, y)) / 3;
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        rGen.init(outputSize, randomize);
        gGen.init(outputSize, randomize);
        bGen.init(outputSize, randomize);
    }
}
