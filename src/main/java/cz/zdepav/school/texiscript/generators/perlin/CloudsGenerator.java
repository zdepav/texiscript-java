package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Rand;

/** Generates basic perlin-based fractal noise. */
public class CloudsGenerator extends PerlinGenerator {

    private final double[] inverseScales, coeficients;
    private final PerlinGradient[] gradients;

    public CloudsGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        super(color1, color2, scale, curve);
        inverseScales = new double[]{
            inverseScale / 4,
            inverseScale / 2,
            inverseScale,
            inverseScale * 2,
            inverseScale * 4,
            inverseScale * 8
        };
        coeficients = new double[]{0.5, 0.25, 0.125, 0.0625, 0.03125, 0.03125};
        gradients = new PerlinGradient[6];
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradients[0] == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            for (var i = 0; i < 6; ++i) {
                gradients[i] = new PerlinGradient(inverseScales[i], rand);
                inverseScales[i] = gradients[i].realScale;
            }
            randomized = randomize;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected double get(double x, double y) {
        double v = 0;
        for (var i = 0; i < 6; ++i) {
            v += gradients[i].perlin(x * inverseScales[i], y * inverseScales[i]) * coeficients[i];
        }
        return v;
    }
}
