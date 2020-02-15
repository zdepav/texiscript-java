package cz.zdepav.school.texiscript.generators.curve;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Curve;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class CurveGenerator extends Generator {

    public final static Generator LINEAR = new CurveGenerator(Curve.linear);

    private final Curve curve;
    private final Generator from, to, min, max;

    public CurveGenerator(Curve curve) {
        this.curve = curve;
        this.from = this.min = Generator.get(0);
        this.to = this.max = Generator.get(1);
    }

    public CurveGenerator(Curve curve, Generator from, Generator to) {
        this.curve = curve;
        this.from = from;
        this.to = to;
        this.min = Generator.get(0);
        this.max = Generator.get(1);
    }

    public CurveGenerator(Curve curve, Generator from, Generator to, Generator min, Generator max) {
        this.curve = curve;
        this.from = from;
        this.to = to;
        this.min = min;
        this.max = max;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var i = getDouble(x, y);
        return new RgbaColor(i);
    }

    @Override
    public double getDouble(double x, double y) {
        var from = this.from.getDouble(x, y);
        var to = this.to.getDouble(x, y);
        var min = this.min.getDouble(x, y);
        var max = this.max.getDouble(x, y);
        double f;
        if (from < to) {
            f = (x - from) / (to - from);
        } else if (to < from) {
            f = (from - x) / (from - to);
        } else /* from == to */ {
            f = x < from ? 0 : 1;
        }
        return Utils.lerp(min, max, curve.at(Utils.clamp(f, 0, 1)));
    }

    public static Generator build(CodePosition pos, Curve curve, String functionName, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 0:
                return new CurveGenerator(curve);
            case 2:
                return new CurveGenerator(curve, args[0], args[1]);
            case 4:
                return new CurveGenerator(curve, args[0], args[1], args[2], args[3]);
            default:
                throw new SemanticException(pos, "curve." + functionName + " requires 0, 2 or 4 arguments");
        }
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        from.init(outputSize, randomize);
        to.init(outputSize, randomize);
        min.init(outputSize, randomize);
        max.init(outputSize, randomize);
    }
}
