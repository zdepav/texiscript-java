package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Rand;

/** Generates velvet texture. */
public class VelvetGenerator extends PerlinGenerator {

    private final PerlinGradient[] gradients;

    public VelvetGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        super(color1, color2, scale, curve);
        gradients = new PerlinGradient[3];
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradients[0] == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            for (var i = 0; i < 3; ++i) {
                gradients[i] = new PerlinGradient(inverseScale, rand);
            }
            inverseScale = gradients[0].realScale;
            randomized = randomize;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected double get(double x, double y) {
        x *= inverseScale;
        y *= inverseScale;
        return gradients[0].perlin(x + gradients[1].perlin(x, y), y + gradients[2].perlin(x, y));
    }
}
