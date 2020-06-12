package cz.zdepav.school.texiscript.utils;

import java.util.List;
import java.util.Random;

/** Implements RandomGenerator interface using java.util.Random */
public class Rand implements RandomGenerator {

    public static final Rand INSTANCE = new Rand();

    /** Underlaying java.util.Random instance */
    private final Random rand;

    public Rand() {
        rand = new Random();
    }

    /** {@inheritDoc} */
    @Override
    public double r() {
        return rand.nextDouble();
    }

    /** {@inheritDoc} */
    @Override
    public double r(double max) {
        return rand.nextDouble() * max;
    }

    /** {@inheritDoc} */
    @Override
    public double r(double min, double max) {
        if (max < min) {
            return min + rand.nextDouble() * (min - max);
        } else {
            return rand.nextDouble() * (max - min) + min;
        }
    }

    /** {@inheritDoc} */
    @Override
    public int i(int max) {
        return rand.nextInt(max);
    }

    /** {@inheritDoc} */
    @Override
    public int i(int min, int max) {
        if (max < min) {
            return min + rand.nextInt(min - max);
        } else {
            return rand.nextInt(max - min) + min;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean chance(double $chance) {
        return rand.nextDouble() < $chance;
    }

    /** {@inheritDoc} */
    @Override
    public int sign() {
        return rand.nextBoolean() ? 1 : -1;
    }

    /** {@inheritDoc} */
    @Override
    public int sign(int num) {
        return rand.nextBoolean() ? num : -num;
    }

    /** {@inheritDoc} */
    @Override
    public double sign(double num) {
        return rand.nextBoolean() ? num : -num;
    }

    /** {@inheritDoc} */
    @Override
    public <T> T item(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        } else if (items.length == 1) {
            return items[0];
        } else {
            return items[rand.nextInt(items.length)];
        }
    }

    /** {@inheritDoc} */
    @Override
    public <T> T item(List<T> items) {
        if (items == null || items.isEmpty()) {
            return null;
        } else if (items.size() == 1) {
            return items.get(0);
        } else {
            return items.get(rand.nextInt(items.size()));
        }
    }
}
