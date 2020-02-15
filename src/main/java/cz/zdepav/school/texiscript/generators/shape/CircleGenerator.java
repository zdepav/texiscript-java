package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Metric;

/** @author Zdenek Pavlatka */
public class CircleGenerator extends ShapeGenerator {

    private final Generator x, y, r;

    public CircleGenerator(CodePosition pos, Generator color, Generator background, Generator x, Generator y, Generator r) {
        super(color, background);
        this.x = x;
        this.y = y;
        this.r = r;
    }

    @Override
    protected boolean isInside(double x, double y) {
        return Metric.euclidean.distance(
            x - this.x.getDouble(x, y),
            y - this.y.getDouble(x, y)
        ) <= r.getDouble(x, y);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        r.init(outputSize, randomize);
    }
}
