package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Curve;
import cz.zdepav.school.texiscript.utils.Utils;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Creates a fish eye effect. */
public class FisheyeGenerator extends TransformationGenerator {

    private final Generator scale, radius, x, y;

    public FisheyeGenerator(
        CodePosition pos,
        Generator base,
        Generator x, Generator y,
        Generator radius,
        Generator scale
    ) {
        super(base);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.scale = scale;
    }

    /** {@inheritDoc} */
    @Override
    protected Vec2 reverseTransform(double x, double y) {
        var v = new Vec2(x, y);
        var _x = this.x.getDouble(x, y);
        var _y = this.y.getDouble(x, y);
        var dv = v.sub(_x, _y);
        if (dv.isZero()) {
            return v;
        }
        var d = dv.length() / this.radius.getDouble(x, y);
        if (d >= 1) {
            return v;
        }
        var scale = this.scale.getDouble(x, y);
        double coef = scale < 0
            ? Utils.lerp(d, Curve.arc.at(d), -scale)
            : Utils.lerp(d, Curve.invArc.at(d), scale);
        return dv.mul(coef / d).add(_x, _y);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        scale.init(outputSize, randomize);
        radius.init(outputSize, randomize);
    }
}
