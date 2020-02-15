package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Rand;

/** @author Zdenek Pavlatka */
public class PerlinNoiseGenerator extends PerlinGenerator {

    private PerlinGradient gradient;

    public PerlinNoiseGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        super(color1, color2, scale, curve);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradient == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            gradient = new PerlinGradient(inverseScale, rand);
            randomized = randomize;
        }
    }

    @Override
    protected double get(double x, double y) {
        return gradient.perlin(x * inverseScale, y * inverseScale);
    }
}
