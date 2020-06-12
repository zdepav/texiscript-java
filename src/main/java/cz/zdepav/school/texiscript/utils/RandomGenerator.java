package cz.zdepav.school.texiscript.utils;

import java.util.List;

/** Random generator interface with some usefull functions. */
public interface RandomGenerator {

    /** Returns random double value between 0 (inclusive) and 1 (exclusive). */
    double r();

    /**
     * Returns random double value between 0 (inclusive) and max (exclusive).
     * @param max Max value (exclusive).
     */
    double r(double max);

    /**
     * Returns random double value between min (inclusive) and max (exclusive).
     * @param min Min value (inclusive).
     * @param max Max value (exclusive).
     */
    double r(double min, double max);

    /**
     * Returns random integer value between 0 (inclusive) and max (exclusive).
     * @param max Max value (exclusive).
     */
    int i(int max);

    /**
     * Returns random integer value between min (inclusive) and max (exclusive).
     * @param min Min value (inclusive).
     * @param max Max value (exclusive).
     */
    int i(int min, int max);

    /**
     * Returns true with the given probability.
     * @param $chance Probability to return true, between 0 and 1.
     */
    boolean chance(double $chance);

    /** Returns 1 or -1 with equal probability. */
    int sign();

    /** Returns num or -num with equal probability. */
    int sign(int num);

    /** Returns num or -num with equal probability. */
    double sign(double num);

    /** Returns a random item from the array or null if it is empty. */
    <T> T item(T[] items);

    /** Returns a random item from the list or null if it is empty. */
    <T> T item(List<T> items);
}
