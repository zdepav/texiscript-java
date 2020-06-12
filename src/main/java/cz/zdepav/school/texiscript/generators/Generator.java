package cz.zdepav.school.texiscript.generators;

import cz.zdepav.school.texiscript.utils.RandomGenerator;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.SeededPositionedRandom;
import cz.zdepav.school.texiscript.utils.Utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/** Base class for image generators. */
public abstract class Generator {

    /** cached number generators */
    private static final Map<Integer, Generator> intGenerators;

    /** cached color generators */
    private static final Map<String, Generator> colorGenerators;

    static {
        intGenerators = new HashMap<>();
        colorGenerators = new HashMap<>();
    }

    /** seed for random generator */
    private int randomSeed;

    protected Generator() {
        randomSeed = 0;
    }

    /**
     * Computes color based on coordinates.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return color for the given location
     */
    public abstract RgbaColor getColor(double x, double y);

    /**
     * Computes value based on coordinates.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return value for the given location
     */
    public abstract double getDouble(double x, double y);

    /**
     * Computes value interpolated between two bounds based on coordinates.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @param min first bound
     * @param max second bound, doesn't have to be greater than min
     * @return value for the given location
     */
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, getDouble(x, y));
    }

    /** Returns true if this generator has a constant numeric value. */
    public boolean isNumber() { return false; }

    /** Returns true if this generator has a constant color value. */
    public boolean isColor() { return false; }

    /**
     * Initializes this generator.
     * @param outputSize width, in pixels, of the generated image
     * @param randomize when true, generator can be fully randomized
     */
    public abstract void init(int outputSize, boolean randomize);

    /** Constructs a deterministic pseudo-random generator. */
    protected final RandomGenerator getRandom() {
        return new SeededPositionedRandom(randomSeed, 3.1415, 9.2653);
    }

    /**
     * Constructs a deterministic pseudo-random generator for given coordinates.
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    protected final RandomGenerator getRandom(double x, double y) {
        return new SeededPositionedRandom(randomSeed, x, y);
    }

    /**
     * Constructs a deterministic pseudo-random generator for given coordinates and with a given seed.
     * @param seed seed for the generator
     * @param x horizontal coordinate
     * @param y vertical coordinate
     */
    protected final RandomGenerator getRandom(double seed, double x, double y) {
        return new SeededPositionedRandom(seed, x, y);
    }

    /** Sets the seed for deterministic pseudo-random generator. */
    public final void setRandomSeed(int randomSeed) {
        this.randomSeed = randomSeed;
    }

    /** Gets a generator for a fixed numeric value. */
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

    /** Gets a generator for a fixed numeric value. */
    public static Generator get(double number) {
        return new NumberGenerator(number);
    }

    /** Gets a generator for a fixed color value. */
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

    /** Gets a generator for a fixed numeric value. */
    public static Generator get(RgbaColor color) {
        return new ColorGenerator(color);
    }

    /** Gets a generator for an input image. */
    public static Generator get(BufferedImage img) {
        return new ImageGenerator(img);
    }
}
