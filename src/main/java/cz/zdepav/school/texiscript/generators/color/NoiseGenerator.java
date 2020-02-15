package cz.zdepav.school.texiscript.generators.color;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Rand;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class NoiseGenerator extends Generator {

    private final Generator base, intensity, saturation;
    private int randomizedSeed;

    public NoiseGenerator(CodePosition pos, Generator base, Generator intensity, Generator saturation) {
        this.base = base;
        this.intensity = intensity;
        this.saturation = saturation;
        randomizedSeed = -1;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x, y).addNoise(
            intensity.getDouble(x, y),
            saturation.getDouble(x, y),
            randomizedSeed < 0 ? getRandom(x, y) : getRandom(randomizedSeed, x, y)
        );
    }

    @Override
    public double getDouble(double x, double y) {
        return base.getColor(x, y).addNoise(
            intensity.getDouble(x, y),
            saturation.getDouble(x, y),
            randomizedSeed < 0 ? getRandom(x, y) : getRandom(randomizedSeed, x, y)
        ).intensity;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        randomizedSeed = randomize ? Rand.INSTANCE.i(10000) : -1;
        base.init(outputSize, randomize);
        intensity.init(outputSize, randomize);
        saturation.init(outputSize, randomize);
    }
}
