package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.utils.RandomGenerator;
import cz.zdepav.school.texiscript.utils.Utils;
import cz.zdepav.school.texiscript.utils.Vec2;

/** @author Zdenek Pavlatka */
public class PerlinGradient {

    private final Vec2[] data;

    public final int size;

    public final double realScale;

    public PerlinGradient(double scale, RandomGenerator rand) {
        size = (int)Math.ceil(256 * scale);
        realScale = size / 256.0;
        var c = size * size;
        data = new Vec2[c];
        for (var i = 0; i < c; ++i) {
            this.data[i] = Vec2.randUnit(rand);
        }
    }

    private Vec2 get(int x, int y) {
        return this.data[Utils.flatten(size, Utils.wrap(x, size), Utils.wrap(y, size))];
    }

    public double dotGridGradient(int ix, int iy, double x, double y) {
        return get(ix, iy).dot(x - ix, y - iy);
    }

    public double perlin(double x, double y) {

        int x0 = (int)Math.floor(x), x1 = x0 + 1;
        int y0 = (int)Math.floor(y), y1 = y0 + 1;
        double sx = x - x0, sy = y - y0;
        return Utils.interpolateSmooth(
            Utils.interpolateSmooth(
                dotGridGradient(x0, y0, x, y),
                dotGridGradient(x1, y0, x, y),
                sx
            ),
            Utils.interpolateSmooth(
                dotGridGradient(x0, y1, x, y),
                dotGridGradient(x1, y1, x, y),
                sx
            ),
            sy
        ) * 1.428;
    }
}
