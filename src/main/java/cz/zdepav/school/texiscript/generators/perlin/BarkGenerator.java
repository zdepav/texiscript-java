package cz.zdepav.school.texiscript.generators.perlin;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Angle;
import cz.zdepav.school.texiscript.utils.Rand;
import cz.zdepav.school.texiscript.utils.Utils;

public class BarkGenerator extends TurbulentPerlinGenerator {

    private final double[] inverseScales, coeficients;
    private final PerlinGradient[] gradients;

    public BarkGenerator(Generator color1, Generator color2, double scale, Generator turbulence, Generator curve) {
        super(color1, color2, 2 * scale, turbulence, curve);
        inverseScales = new double[]{
            inverseScale,
            inverseScale * 2,
            inverseScale * 4,
            inverseScale * 6
        };
        coeficients = new double[]{0.5, 0.25, 0.25};
        gradients = new PerlinGradient[4];
    }

    @Override
    protected double get(double x, double y) {
        var v = 0.0;
        var turbulence = 0.125 * this.turbulence.getDouble(x, y);
        for (var i = 0; i < 3; ++i) {
            v += gradients[i].perlin(
                x * inverseScales[i],
                y * inverseScales[i]
            ) * coeficients[i] * turbulence;
        }
        v = Utils.granulate(Math.sin(x * inverseScale * Angle.deg360 + 8 * v), 2);
        v += Utils.granulate(gradients[3].perlin(x * inverseScales[3], y * inverseScales[3]), 5);
        return v / 2;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        if (gradients[0] == null || randomize || randomized) {
            var rand = randomize ? Rand.INSTANCE : getRandom();
            for (var i = 0; i < 4; ++i) {
                gradients[i] = new PerlinGradient(inverseScales[i], rand);
            }
            randomized = randomize;
        }
    }
}
