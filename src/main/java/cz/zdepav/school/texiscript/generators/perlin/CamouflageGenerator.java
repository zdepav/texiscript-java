package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Rand;
import cz.zdepav.school.texiscript.utils.Utils;

/** Generates camouflage-style pattern. */
public class CamouflageGenerator extends PerlinGenerator {

    private final double[] inverseScales, coeficients;

    private final PerlinGradient[] gradients;

    public CamouflageGenerator(Generator color1, Generator color2, double scale, Generator curve) {
        super(color1, color2, scale, curve);
        inverseScales = new double[]{inverseScale, inverseScale * 2, inverseScale * 4};
        coeficients = new double[]{1.5, 0.75, 0.75};
        gradients = new PerlinGradient[9];
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradients[0] == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            for (var i = 0; i < 9; ++i) {
                gradients[i] = new PerlinGradient(inverseScales[i % 3], rand);
            }
            for (var i = 0; i < 3; ++i) {
                inverseScales[i] = gradients[i].realScale;
            }
            randomized = randomize;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected double get(double x, double y) {
        var _x = x * inverseScale;
        var _y = y * inverseScale;
        for (var i = 0; i < 3; ++i) {
            _x += gradients[i].perlin(x * inverseScales[i], y * inverseScales[i]) * coeficients[i];
            _y += gradients[i + 3].perlin(x * inverseScales[i], y * inverseScales[i]) * coeficients[i];
        }
        return Utils.granulate(gradients[6].perlin(_x, _y), 4) * 0.7
            + Utils.granulate(gradients[7].perlin(_x * 2, _y * 2), 5) * 0.2
            + Utils.granulate(gradients[8].perlin(_x * 4, _y * 4), 6) * 0.1;
    }
}
