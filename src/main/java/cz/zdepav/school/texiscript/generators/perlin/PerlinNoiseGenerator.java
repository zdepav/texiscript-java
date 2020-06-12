package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Rand;

/** Generates basic perlin noise. */
public class PerlinNoiseGenerator extends PerlinGenerator {

    private PerlinGradient gradient;

    public PerlinNoiseGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        super(color1, color2, scale, curve);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradient == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            gradient = new PerlinGradient(inverseScale, rand);
            inverseScale = gradient.realScale;
            randomized = randomize;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected double get(double x, double y) {
        return gradient.perlin(x * inverseScale, y * inverseScale);
    }
}
