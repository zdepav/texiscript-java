package cz.zdepav.school.texiscript.generators.gradients;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class GradientStopGenerator extends Generator implements Comparable<GradientStopGenerator> {

    public final double pos;
    private final Generator color;

    public GradientStopGenerator(CodePosition pos, Generator position, Generator color) throws SemanticException {
        if (!position.isNumber()) {
            throw new SemanticException(pos, "First argument of gradient.stop must be a number");
        }
        this.pos = position.getDouble(0, 0);
        this.color = color;
    }

    @Override
    public int compareTo(GradientStopGenerator o) {
        return Double.compare(pos, o.pos);
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return color.getColor(x, y);
    }

    @Override
    public double getDouble(double x, double y) {
        return color.getDouble(x, y);
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return color.getDouble(x, y, min, max);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        color.init(outputSize, randomize);
    }
}
