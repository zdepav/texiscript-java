package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Rand;

/** @author Zdenek Pavlatka */
public class GlassGenerator extends TurbulentPerlinGenerator {

    private final PerlinGradient[] gradients;

    public GlassGenerator(Generator color1, Generator color2, double scale, Generator turbulence, Generator curve) {
        super(color1, color2, 2 * scale, turbulence, curve);
        gradients = new PerlinGradient[3];
    }

    @Override
    protected double get(double x, double y) {
        x *= inverseScale;
        y *= inverseScale;
        var turbulence = 0.125 * this.turbulence.getDouble(x, y);
        var _x = Math.cos((gradients[1].perlin(x, y) * 128 + 128) * turbulence);
        var _y = Math.sin((gradients[2].perlin(x, y) * 128 + 128) * turbulence);
        return gradients[0].perlin(x + _x, y + _y);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradients[0] == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            for (var i = 0; i < 3; ++i) {
                gradients[i] = new PerlinGradient(inverseScale, rand);
            }
            randomized = randomize;
        }
    }
}
