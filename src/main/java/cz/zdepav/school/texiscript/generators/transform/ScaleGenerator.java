package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Scales its input. */
public class ScaleGenerator extends TransformationGenerator {

    private final Generator xScale, yScale, x, y;

    private ScaleGenerator(Generator base, Generator x, Generator y, Generator xScale, Generator yScale) {
        super(base);
        this.x = x;
        this.y = y;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    /** {@inheritDoc} */
    @Override
    protected Vec2 reverseTransform(double x, double y) {
        var scaleX = xScale.getDouble(x, y);
        var scaleY = xScale == yScale ? scaleX : yScale.getDouble(x, y);
        var v = new Vec2(x, y);
        var origin = new Vec2(this.x.getDouble(x, y), this.y.getDouble(x, y));
        var dv = v.sub(origin);
        return dv.isZero() ? v : origin.add(dv.x / scaleX, dv.y / scaleY);
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 4:
                return new ScaleGenerator(args[0], args[1], args[2], args[3], args[3]);
            case 5:
                return new ScaleGenerator(args[0], args[1], args[2], args[3], args[4]);
            default:
                throw new SemanticException(pos, "transforn.scale requires 4 or 5 arguments");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        xScale.init(outputSize, randomize);
        yScale.init(outputSize, randomize);
    }
}
