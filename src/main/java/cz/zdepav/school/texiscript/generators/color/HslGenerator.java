package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Converts HSL to a color. */
public class HslGenerator extends Generator {

    /** hue input generator */
    private final Generator hGen;

    /** saturation input generator */
    private final Generator sGen;

    /** lightness input generator */
    private final Generator lGen;

    public HslGenerator(CodePosition pos, Generator hGen, Generator sGen, Generator lGen) {
        this.hGen = hGen;
        this.sGen = sGen;
        this.lGen = lGen;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var l = lGen.getDouble(x, y);
        var h = Utils.wrap(hGen.getDouble(x, y)) * 6;
        var c = (1 - Math.abs(2 * l - 1)) * sGen.getDouble(x, y);
        var d = c * (1 - Math.abs(h % 2 - 1));
        var m = l - c / 2;
        switch ((int)h) {
            case 0: return new RgbaColor(c + m, d + m, m);
            case 1: return new RgbaColor(d + m, c + m, m);
            case 2: return new RgbaColor(m, c + m, d + m);
            case 3: return new RgbaColor(m, d + m, c + m);
            case 4: return new RgbaColor(d + m, m, c + m);
            default: return new RgbaColor(c + m, m, d + m);
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var l = lGen.getDouble(x, y);
        var h = Utils.wrap(hGen.getDouble(x, y)) * 6;
        var c = (1 - Math.abs(2 * l - 1)) * sGen.getDouble(x, y);
        return (c * (2 - Math.abs(h % 2 - 1))) / 3 + l - c / 2;
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        hGen.init(outputSize, randomize);
        sGen.init(outputSize, randomize);
        lGen.init(outputSize, randomize);
    }
}
