package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class HslaGenerator extends Generator {

    private final Generator hGen, sGen, lGen, aGen;

    public HslaGenerator(CodePosition pos, Generator hGen, Generator sGen, Generator lGen, Generator aGen) {
        this.hGen = hGen;
        this.sGen = sGen;
        this.lGen = lGen;
        this.aGen = aGen;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var l = lGen.getDouble(x, y);
        var h = Utils.wrap(hGen.getDouble(x, y)) * 6;
        var c = (1 - Math.abs(2 * l - 1)) * sGen.getDouble(x, y);
        var d = c * (1 - Math.abs(h % 2 - 1));
        var m = l - c / 2;
        var a = aGen.getDouble(x, y);
        switch ((int)h) {
            case 0: return new RgbaColor(c + m, d + m, m, a);
            case 1: return new RgbaColor(d + m, c + m, m, a);
            case 2: return new RgbaColor(m, c + m, d + m, a);
            case 3: return new RgbaColor(m, d + m, c + m, a);
            case 4: return new RgbaColor(d + m, m, c + m, a);
            default: return new RgbaColor(c + m, m, d + m, a);
        }
    }

    @Override
    public double getDouble(double x, double y) {
        var l = lGen.getDouble(x, y);
        var h = Utils.wrap(hGen.getDouble(x, y)) * 6;
        var c = (1 - Math.abs(2 * l - 1)) * sGen.getDouble(x, y);
        return (c * (2 - Math.abs(h % 2 - 1))) / 3 + l - c / 2;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        hGen.init(outputSize, randomize);
        sGen.init(outputSize, randomize);
        lGen.init(outputSize, randomize);
        aGen.init(outputSize, randomize);
    }
}
