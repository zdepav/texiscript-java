package cz.zdepav.school.texiscript.generators.shape;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Metric;

/** @author Zdenek Pavlatka */
public class EllipseGenerator extends ShapeGenerator {

    private final Generator x, y, r1, r2;

    public EllipseGenerator(
        CodePosition pos,
        Generator color, Generator background,
        Generator x, Generator y,
        Generator r1, Generator r2
    ) {
        super(color, background);
        this.x = x;
        this.y = y;
        this.r1 = r1;
        this.r2 = r2;
    }

    @Override
    protected boolean isInside(double x, double y) {
        return Metric.euclidean.distance(
            (x - this.x.getDouble(x, y)) / r1.getDouble(x, y),
            (y - this.y.getDouble(x, y)) / r2.getDouble(x, y)
        ) <= 1;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
        r1.init(outputSize, randomize);
        r2.init(outputSize, randomize);
    }
}
