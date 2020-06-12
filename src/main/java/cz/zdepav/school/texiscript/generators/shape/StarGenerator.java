package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Angle;
import cz.zdepav.school.texiscript.utils.Utils;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Generates a star. */
public class StarGenerator extends ShapeGenerator {

    private final int corners;

    private final Generator x, y, r, stuffing;

    public StarGenerator(
        Generator color, Generator background,
        Generator x, Generator y, Generator r,
        int corners, Generator stuffing,
        Generator bevel, Generator curve
    ) {
        super(color, background, bevel, curve);
        this.x = x;
        this.y = y;
        this.r = r;
        this.corners = corners;
        this.stuffing = stuffing;
    }

    /** {@inheritDoc} */
    @Override
    protected double distanceFromCenter(double x, double y) {
        var r2 = r.getDouble(x, y);
        if (r2 <= 0) {
            return 2.0;
        }
        var s = Math.min(stuffing.getDouble(x, y), 1.0);
        if (s <= 0) {
            return 2.0;
        }
        var center = new Vec2(this.x.getDouble(x, y), this.y.getDouble(x, y));
        var dir = ((center.directionTo(x, y) / Angle.deg360 + 1.25) % 1) * corners * 2;
        if (Math.abs(dir - Math.round(dir)) < 0.00001) {
            var dist = center.distanceTo(x, y);
            return dist / (dir % 2 >= 1 ? r2 * s : r2);
        }
        double r1;
        if (dir % 2 >= 1) {
            r1 = r2 * s;
        } else {
            r1 = r2;
            r2 *= s;
        }
        var w = Angle.deg180 / corners;
        var a = center.addld(r1, Math.floor(dir) * w + Angle.deg270);
        var b = center.addld(r2, (Math.floor(dir) + 1) * w + Angle.deg270);
        var bc = new Vec2(x, y).coordsInTriangle(center, a, b);
        return bc.allNonNegative() ? 1 - bc.x : 2.0;
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        r.init(outputSize, randomize);
        stuffing.init(outputSize, randomize);
    }

    /**
     * Builds the generator.
     * @param pos current position in script
     * @param args function arguments
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(
        CodePosition pos,
        Generator[] args
    ) throws SemanticException {
        if (args.length < 7 || args.length > 9) {
            throw new SemanticException(pos, "shape.star requires 7 to 9 arguments");
        }
        int points;
        if (args[5].isNumber()) {
            points = Utils.clamp((int)args[5].getDouble(0, 0), 2, 1000);
        } else {
            throw new SemanticException(pos, "shape.star expects a number as it's sixth argument.");
        }
        var bevel = args.length > 7 ? args[7] : Generator.get(0);
        var curve = args.length == 9 ? args[8] : CurveGenerator.LINEAR;
        return new StarGenerator(args[0], args[1], args[2], args[3], args[4], points, args[6], bevel, curve);
    }
}
