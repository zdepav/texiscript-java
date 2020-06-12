package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Rand;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Adds random noise to its input. */
public class NoiseGenerator extends Generator {

    /** input generator */
    private final Generator base;

    /** noise intensity generator */
    private final Generator intensity;

    /** noise color saturation generator */
    private final Generator saturation;

    /** random generator seed if randomization is enabled, -1 otherwise */
    private int randomizedSeed;

    public NoiseGenerator(CodePosition pos, Generator base, Generator intensity, Generator saturation) {
        this.base = base;
        this.intensity = intensity;
        this.saturation = saturation;
        randomizedSeed = -1;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).addNoise(
            intensity.getDouble(x, y),
            saturation.getDouble(x, y),
            randomizedSeed < 0 ? getRandom(x, y) : getRandom(randomizedSeed, x, y)
        );
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return base.getColor(x, y).addNoise(
            intensity.getDouble(x, y),
            saturation.getDouble(x, y),
            randomizedSeed < 0 ? getRandom(x, y) : getRandom(randomizedSeed, x, y)
        ).intensity;
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        randomizedSeed = randomize ? Rand.INSTANCE.i(10000) : -1;
        base.init(outputSize, randomize);
        intensity.init(outputSize, randomize);
        saturation.init(outputSize, randomize);
    }
}
