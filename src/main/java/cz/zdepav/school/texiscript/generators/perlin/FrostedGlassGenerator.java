package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Rand;

/** @author Zdenek Pavlatka */
public class FrostedGlassGenerator extends PerlinGenerator {

    private final double[] inverseScales, coeficients;
    private final PerlinGradient[] gradients;

    public FrostedGlassGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        super(color1, color2, scale, curve);
        inverseScales = new double[]{inverseScale, inverseScale * 2, inverseScale * 4};
        coeficients = new double[]{0.5, 0.25, 0.25};
        gradients = new PerlinGradient[7];
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradients[0] == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            for (var i = 0; i < 7; ++i) {
                gradients[i] = new PerlinGradient(inverseScales[i % 3], rand);
            }
            randomized = randomize;
        }
    }

    @Override
    protected double get(double x, double y) {
        var _x = x * inverseScale;
        var _y = y * inverseScale;
        for (var i = 0; i < 3; ++i) {
            _x += gradients[i].perlin(x * inverseScales[i], y * inverseScales[i]) * coeficients[i];
            _y += gradients[i + 3].perlin(x * inverseScales[i], y * inverseScales[i]) * coeficients[i];
        }
        return gradients[6].perlin(_x, _y);
    }
}
