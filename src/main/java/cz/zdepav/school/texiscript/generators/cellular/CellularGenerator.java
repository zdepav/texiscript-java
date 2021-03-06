package cz.zdepav.school.texiscript.generators.cellular;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Metric;
import cz.zdepav.school.texiscript.utils.Rand;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Base class for cellular generators. */
public abstract class CellularGenerator extends Generator {

    /** Interface for cellular generator constructors. */
    @FunctionalInterface
    public interface BasicConstructor {

        /**
         * Constructs a cellular generator.
         * @param density determines how many cells will be generated (cell count is second power of density)
         * @param metric metric to use for distance measurement
         * @param color1 grid color
         * @param color2 fill color
         * @param chaos with lower chaos the cells will be closer to a 2d grid
         * @param curve curve for color interpolation
         * @return the constructed generator
         */
        CellularGenerator construct(
            double density, Metric metric,
            Generator color1, Generator color2,
            double chaos, Generator curve
        );
    }

    /** base points for cells */
    protected final Vec2[] points;

    /** grid color */
    protected final Generator color1;

    /** fill color */
    protected final Generator color2;

    /** curve for color interpolation */
    protected final Generator curve;

    /** with lower chaos the cells will be closer to a 2d grid */
    protected final double chaos;

    /** cell size (equal to cell width with chaos equal to 0) */
    protected final double tileSize;

    /** determines how many cells will be generated (cell count is second power of density) */
    protected final int density;

    /** metric to use for distance measurement */
    protected final Metric metric;

    /** whether generation should be fully randomized or deterministic */
    protected boolean randomized;

    /** maximum computed distance (value returned by getDistance) to a point */
    protected double maxDist;

    protected CellularGenerator(
        double density, Metric metric,
        Generator color1, Generator color2,
        double chaos, Generator curve
    ) {
        this.density = Utils.clamp((int)density, 2, 256);
        tileSize = 1.0 / this.density;
        points = new Vec2[this.density * this.density];
        this.metric = metric;
        this.color1 = color1;
        this.color2 = color2;
        this.chaos = Utils.clamp(chaos, 0.0, 1.0);
        this.curve = curve;
        randomized = false;
        maxDist = 1;
    }

    /**
     * Computes distance to the nearest instance of the given point.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @param p point to measure distance from, the point is repeated with interval 1 on each direction on each axis
     * @return computed distance
     */
    protected double wrappedDistance(double x, double y, Vec2 p) {
        var dx = Math.abs(x - p.x);
        var dy = Math.abs(y - p.y);
        return metric.distance(dx > 0.5 ? 1 - dx : dx, dy > 0.5 ? 1 - dy : dy);
    }

    /**
     * Gets distances to two nearest points.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @param nearPts 9 points to check
     * @return 2-item array with computed distances
     */
    protected double[] getDistancesTo2Nearest(double x, double y, Vec2[] nearPts) {
        var min1 = Double.POSITIVE_INFINITY;
        var min2 = Double.POSITIVE_INFINITY;
        for (var i = 0; i < 9; ++i) {
            var d = wrappedDistance(x, y, nearPts[i]);
            if (d < min1) {
                min2 = min1;
                min1 = d;
            } else if (d < min2) {
                min2 = d;
            }
        }
        return new double[] { min1, min2 };
    }

    /**
     * Computes value used to generate the cellular pattern.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @param distancesTo2Nearest array returned by getDistancesTo2Nearest
     * @return computed value
     */
    protected abstract double getDistance(double x, double y, double[] distancesTo2Nearest);

    /**
     * Computes value used to generate the cellular pattern.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return computed value
     */
    protected double getDistance(double x, double y) {
        var _x = (int)(x * density);
        var _y = (int)(y * density);
        var nearPts = new Vec2[9];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                nearPts[i + j * 3] =
                    points[Utils.wrap(_y + j - 1, density) * density + Utils.wrap(_x + i - 1, density)];
            }
        }
        return getDistance(x, y, getDistancesTo2Nearest(x, y, nearPts));
    }

    /** Computes maximum distance (value returned by getDistance) to a point. */
    protected void getMaxDistance() {
        maxDist = 0.00001;
        for (var x = 0.0; x <= 1; x += 0.001) {
            for (var y = 0.0; y <= 1; y += 0.001) {
                maxDist = Math.max(maxDist, getDistance(x, y));
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return color1.getColor(x, y).lerp(
            color2.getColor(x, y),
            curve.getDouble(1 - getDistance(x, y) / maxDist, y)
        );
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return Utils.lerp(
            color1.getDouble(x, y),
            color2.getDouble(x, y),
            curve.getDouble(1 - getDistance(x, y) / maxDist, y)
        );
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        color1.init(outputSize, randomize);
        color2.init(outputSize, randomize);
        curve.init(outputSize, randomize);
        if (points[0] == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            var halfTile = tileSize / 2;
            for (var i = 0; i < density; ++i) {
                for (var j = 0; j < density; ++j) {
                    points[i + j * density] = new Vec2(
                        (double)i / density + Utils.lerp(halfTile, rand.r(tileSize), chaos),
                        (double)j / density + Utils.lerp(halfTile, rand.r(tileSize), chaos)
                    );
                }
            }
            randomized = randomize;
            getMaxDistance();
        }
    }

    /**
     * Builds a cellular generator with given name from arguments using it's constructor.
     * @param pos current position in script
     * @param args function arguments
     * @param name generator name
     * @param constructor generator constructor to use
     * @return created generator
     * @throws SemanticException When the arguments are not valid.
     */
    public static Generator build(
        CodePosition pos,
        Generator[] args,
        String name,
        BasicConstructor constructor
    ) throws SemanticException {
        if (args.length == 0) {
            return constructor.construct(
                20, Metric.euclidean,
                RgbaColor.black.generator(),
                RgbaColor.white.generator(),
                1, CurveGenerator.LINEAR
            );
        }
        if (!args[0].isNumber()) {
            throw new SemanticException(pos, "cellular." + name + " expects a number as its first argument");
        }
        var density = args[0].getDouble(0, 0);
        if (args.length == 1) {
            return constructor.construct(
                density,
                Metric.euclidean,
                RgbaColor.black.generator(),
                RgbaColor.white.generator(),
                1, CurveGenerator.LINEAR
            );
        }
        if (!args[1].isNumber()) {
            throw new SemanticException(pos, "cellular." + name + " expects a number as its second argument");
        }
        var m = Metric.euclidean;
        switch ((int)args[1].getDouble(0, 0)) {
            case 1:
                m = Metric.manhattan;
                break;
            case 2:
                m = Metric.chebyshev;
                break;
            case 3:
                m = Metric.minkowski;
                break;
        }
        if (args.length > 4 && !args[4].isNumber()) {
            throw new SemanticException(pos, "cellular." + name + " expects a number as its fifth argument");
        }
        switch (args.length) {
            case 2:
                return constructor.construct(
                    density, m,
                    RgbaColor.black.generator(),
                    RgbaColor.white.generator(),
                    1, CurveGenerator.LINEAR
                );
            case 3:
                return constructor.construct(
                    density, m,
                    RgbaColor.black.generator(),
                    args[2], 1,
                    CurveGenerator.LINEAR
                );
            case 4:
                return constructor.construct(density, m, args[2], args[3], 1, CurveGenerator.LINEAR);
            case 5:
                return constructor.construct(
                    density, m, args[2], args[3],
                    args[4].getDouble(0, 0),
                    CurveGenerator.LINEAR);
            case 6:
                return constructor.construct(density, m, args[2], args[3], args[4].getDouble(0, 0), args[5]);
            default:
                throw new SemanticException(pos, "cellular." + name + " requires 0 to 6 arguments");
        }
    }
}
