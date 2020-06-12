package cz.zdepav.school.texiscript.utils;

import java.util.List;

/** RandomGenerator implementation that generates values based on a seed and two coordinates  */
public class SeededPositionedRandom implements RandomGenerator {

    /** previously generated value or generator seed if no value was generated yet */
    private double seed;

    /** x coordinate */
    private final double x;

    /** y coordinate */
    private final double y;

    public SeededPositionedRandom(double seed, double x, double y) {
        this.seed = seed;
        this.x = x;
        this.y = y;
    }

    /** {@inheritDoc} */
    @Override
    public double r() {
        seed = (1 + Math.cos(
            123456789.0 % (1e-7 + 256 * (x * 23.14069263277926 + y * 2.665144142690225)) + seed
        )) * 12345.6789 % 1;
        return seed;
    }

    /** {@inheritDoc} */
    @Override
    public double r(double max) {
        return r() * max;
    }

    /** {@inheritDoc} */
    @Override
    public double r(double min, double max) {
        return min + r() * (max - min);
    }

    /** {@inheritDoc} */
    @Override
    public int i(int max) {
        return (int)(r() * max);
    }

    /** {@inheritDoc} */
    @Override
    public int i(int min, int max) {
        return min + (int)(r() * (max - min));
    }

    /** {@inheritDoc} */
    @Override
    public boolean chance(double $chance) {
        return r() < $chance;
    }

    /** {@inheritDoc} */
    @Override
    public int sign() {
        return r() < 0.5 ? -1 : 1;
    }

    /** {@inheritDoc} */
    @Override
    public int sign(int num) {
        return r() < 0.5 ? -num : num;
    }

    /** {@inheritDoc} */
    @Override
    public double sign(double num) {
        return r() < 0.5 ? -num : num;
    }

    /** {@inheritDoc} */
    @Override
    public <T> T item(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        } else if (items.length == 1) {
            return items[0];
        } else {
            return items[i(items.length)];
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
            return items.get(i(items.size()));
        }
    }
}
