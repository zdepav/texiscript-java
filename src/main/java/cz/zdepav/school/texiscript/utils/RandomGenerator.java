package cz.zdepav.school.texiscript.utils;

import java.util.List;

public interface RandomGenerator {

    /**
     * returns random double value between 0 (inclusive) and 1 (exclusive)
     */
    double r();

    /**
     * returns random double value between 0 (inclusive) and max (exclusive)
     * @param max max value (exclusive)
     */
    double r(double max);

    /**
     * returns random double value between min (inclusive) and max (exclusive)
     * @param min min value (inclusive)
     * @param max max value (exclusive)
     */
    double r(double min, double max);

    /**
     * returns random integer value between 0 (inclusive) and max (exclusive)
     * @param max max value (exclusive)
     */
    int i(int max);

    /**
     * returns random integer value between min (inclusive) and max (exclusive)
     * @param min min value (inclusive)
     * @param max max value (exclusive)
     */
    int i(int min, int max);

    /**
     * @param $chance probability to return true, between 0 and 1
     */
    boolean chance(double $chance);

    int sign();

    int sign(int num);

    double sign(double num);

    <T> T item(T[] items);

    <T> T item(List<T> items);
}
