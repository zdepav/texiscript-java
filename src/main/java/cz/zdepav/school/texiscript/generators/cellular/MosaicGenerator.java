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
import cz.zdepav.school.texiscript.utils.Vec3;

/** @author Zdenek Pavlatka */
public class MosaicGenerator extends Generator {

    protected final Vec3[] points;
    protected final Generator color1, color2, curve;
    protected final double chaos, tileSize;
    protected final int density;
    protected final Metric metric;

    protected boolean randomized;

    protected MosaicGenerator(
        double density, Metric metric,
        Generator color1, Generator color2,
        double chaos, Generator curve
    ) {
        this.density = Utils.lerpInt(1, 128, density / 2);
        tileSize = 1.0 / this.density;
        points = new Vec3[this.density * this.density];
        this.metric = metric;
        this.color1 = color1;
        this.color2 = color2;
        this.chaos = Utils.clamp(chaos, 0.0, 1.0);
        this.curve = curve;
        randomized = false;
    }

    protected double wrappedDistance(double x, double y, Vec2 p) {
        var dx = Math.abs(x - p.x);
        var dy = Math.abs(y - p.y);
        return metric.distance(dx > 0.5 ? 1 - dx : dx, dy > 0.5 ? 1 - dy : dy);
    }

    private Vec3 getNearest(double x, double y, Vec3[] nearPts) {
        var pt = nearPts[0];
        var min = wrappedDistance(x, y, pt.xy());
        for (var i = 1; i < 9; ++i) {
            var d = wrappedDistance(x, y, nearPts[i].xy());
            if (d < min) {
                min = d;
                pt = nearPts[i];
            }
        }
        return pt;
    }

    private double getDistance(double x, double y) {
        var _x = (int)(x * density);
        var _y = (int)(y * density);
        var nearPts = new Vec3[9];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                nearPts[i + j * 3] =
                    points[Utils.wrap(_y + j - 1, density) * density + Utils.wrap(_x + i - 1, density)];
            }
        }
        return getNearest(x, y, nearPts).z;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return color1.getColor(x, y).lerp(color2.getColor(x, y), getDistance(x, y));
    }

    @Override
    public double getDouble(double x, double y) {
        return Utils.lerp(color1.getDouble(x, y), color2.getDouble(x, y), getDistance(x, y));
    }

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
                    points[i + j * density] = new Vec3(
                        (double)i / density + Utils.lerp(halfTile, rand.r(tileSize), chaos),
                        (double)j / density + Utils.lerp(halfTile, rand.r(tileSize), chaos),
                        rand.r()
                    );
                }
            }
            randomized = randomize;
        }
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        if (args.length == 0) {
            return new MosaicGenerator(
                0.5, Metric.euclidean,
                RgbaColor.black.generator(),
                RgbaColor.white.generator(),
                1, CurveGenerator.LINEAR
            );
        }
        if (!args[0].isNumber()) {
            throw new SemanticException(pos, "cellular.mosaic expects a number as its first argument");
        }
        var density = args[0].getDouble(0, 0);
        if (args.length == 1) {
            return new MosaicGenerator(
                density,
                Metric.euclidean,
                RgbaColor.black.generator(),
                RgbaColor.white.generator(),
                1, CurveGenerator.LINEAR
            );
        }
        if (!args[1].isNumber()) {
            throw new SemanticException(pos, "cellular.mosaic expects a number as its second argument");
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
            throw new SemanticException(pos, "cellular.mosaic expects a number as its fifth argument");
        }
        switch (args.length) {
            case 2:
                return new MosaicGenerator(
                    density, m,
                    RgbaColor.black.generator(),
                    RgbaColor.white.generator(),
                    1, CurveGenerator.LINEAR
                );
            case 3:
                return new MosaicGenerator(
                    density, m,
                    RgbaColor.black.generator(),
                    args[2], 1,
                    CurveGenerator.LINEAR
                );
            case 4:
                return new MosaicGenerator(density, m, args[2], args[3], 1, CurveGenerator.LINEAR);
            case 5:
                return new MosaicGenerator(
                    density, m, args[2], args[3],
                    args[4].getDouble(0, 0),
                    CurveGenerator.LINEAR);
            case 6:
                return new MosaicGenerator(density, m, args[2], args[3], args[4].getDouble(0, 0), args[5]);
            default:
                throw new SemanticException(pos, "cellular.mosaic requires 0 to 6 arguments");
        }
    }
}
