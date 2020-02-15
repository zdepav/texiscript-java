package cz.zdepav.school.texiscript.generators;

import cz.zdepav.school.texiscript.utils.RandomGenerator;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.SeededPositionedRandom;
import cz.zdepav.school.texiscript.utils.Utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/** @author Zdenek Pavlatka */
public abstract class Generator {

    private static Map<Integer, Generator> intGenerators;
    private static Map<String, Generator> colorGenerators;

    static {
        intGenerators = new HashMap<>();
        colorGenerators = new HashMap<>();
    }

    private int randomSeed;

    protected Generator() {
        randomSeed = 0;
    }

    public abstract RgbaColor getColor(double x, double y);

    // max doesn't have to be greater than min!
    public abstract double getDouble(double x, double y);

    // max doesn't have to be greater than min!
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, getDouble(x, y));
    }

    public boolean isNumber() { return false; }

    public boolean isColor() { return false; }

    public abstract void init(int outputSize, boolean randomize);

    protected final RandomGenerator getRandom() {
        return new SeededPositionedRandom(randomSeed, 3.1415, 9.2653);
    }

    protected final RandomGenerator getRandom(double x, double y) {
        return new SeededPositionedRandom(randomSeed, x, y);
    }

    protected final RandomGenerator getRandom(double seed, double x, double y) {
        return new SeededPositionedRandom(randomSeed, x, y);
    }

    public final void setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
    }

    public static Generator get(int number) {
        var gen = intGenerators.getOrDefault(number, null);
        if (gen == null) {
            gen = new NumberGenerator(number);
            if (intGenerators.size() < 1000) {
                intGenerators.put(number, gen);
            }
        }
        return gen;
    }

    public static Generator get(double number) {
        return new NumberGenerator(number);
    }

    public static Generator get(String color) {
        var gen = colorGenerators.getOrDefault(color, null);
        if (gen == null) {
            gen = new ColorGenerator(RgbaColor.parseHex(color));
            if (colorGenerators.size() < 1000) {
                colorGenerators.put(color, gen);
            }
        }
        return gen;
    }

    public static Generator get(RgbaColor color) {
        return new ColorGenerator(color);
    }

    public static Generator get(BufferedImage img) {
        return new ImageGenerator(img);
    }
}
