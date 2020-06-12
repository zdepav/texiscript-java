package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Angle;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Rotates its input. */
public class RotationGenerator extends TransformationGenerator {

    private final Generator angle, x, y;

    public RotationGenerator(CodePosition pos, Generator base, Generator x, Generator y, Generator angle) {
        super(base);
        this.angle = angle;
        this.x = x;
        this.y = y;
    }

    /** {@inheritDoc} */
    @Override
    protected Vec2 reverseTransform(double x, double y) {
        return new Vec2(x, y).rotateAround(
            new Vec2(this.x.getDouble(x, y), this.y.getDouble(x, y)),
            -this.angle.getDouble(x, y) * Angle.deg360
        );
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        angle.init(outputSize, randomize);
    }
}
