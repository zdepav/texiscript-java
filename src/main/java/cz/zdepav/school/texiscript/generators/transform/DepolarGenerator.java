package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Angle;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Converts from polar to standard coordinates. */
public class DepolarGenerator extends TransformationGenerator {

    private final Generator x, y;

    public DepolarGenerator(CodePosition pos, Generator base, Generator x, Generator y) {
        super(base);
        this.x = x;
        this.y = y;
    }

    /** {@inheritDoc} */
    @Override
    protected Vec2 reverseTransform(double x, double y) {
        var origin = new Vec2(this.x.getDouble(x, y), this.y.getDouble(x, y));
        return new Vec2(
            origin.directionTo(x, y) / Angle.deg360,
            new Vec2(x, y).sub(origin).length() * 2
        );
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
    }
}
