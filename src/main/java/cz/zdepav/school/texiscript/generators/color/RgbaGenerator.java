package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class RgbaGenerator extends Generator {

    private final Generator rGen, gGen, bGen, aGen;

    public RgbaGenerator(CodePosition pos, Generator rGen, Generator gGen, Generator bGen, Generator aGen) {
        this.rGen = rGen;
        this.gGen = gGen;
        this.bGen = bGen;
        this.aGen = aGen;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(rGen.getDouble(x, y), gGen.getDouble(x, y), bGen.getDouble(x, y), aGen.getDouble(x, y));
    }

    @Override
    public double getDouble(double x, double y) {
        return (rGen.getDouble(x, y) + gGen.getDouble(x, y) + bGen.getDouble(x, y)) / 3;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        rGen.init(outputSize, randomize);
        gGen.init(outputSize, randomize);
        bGen.init(outputSize, randomize);
        aGen.init(outputSize, randomize);
    }
}
