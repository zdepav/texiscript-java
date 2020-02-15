package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class SetAlphaGenerator extends Generator {

    private final Generator rgbGen, aGen;

    public SetAlphaGenerator(CodePosition pos, Generator rgbGen, Generator aGen) {
        this.rgbGen = rgbGen;
        this.aGen = aGen;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var col = rgbGen.getColor(x, y);
        return new RgbaColor(col.r, col.g, col.b, aGen.getDouble(x, y));
    }

    @Override
    public double getDouble(double x, double y) {
        return rgbGen.getDouble(x, y);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        rgbGen.init(outputSize, randomize);
        aGen.init(outputSize, randomize);
    }
}
