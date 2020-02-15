package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class SepiaGenerator extends Generator {

    private final Generator base;

    public SepiaGenerator(CodePosition pos, Generator base) {
        this.base = base;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var col = base.getColor(x, y);
        return new RgbaColor(
            col.r * 0.393 + col.g * 0.769 + col.b * 0.189,
            col.r * 0.349 + col.g * 0.686 + col.b * 0.168,
            col.r * 0.272 + col.g * 0.534 + col.b * 0.131,
            col.a
        );
    }

    @Override
    public double getDouble(double x, double y) {
        var col = base.getColor(x, y);
        return col.r * 0.338 + col.g * 0.663 + col.b * 0.163;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }
}
