package cz.zdepav.school.texiscript.utils;

import java.util.List;
import java.util.Random;

/** @author Zdenek Pavlatka */
public class Rand implements RandomGenerator {

    public static final Rand INSTANCE = new Rand();

    private final Random rand;

    public Rand() {
        rand = new Random();
    }

    public double r() {
        return rand.nextDouble();
    }

    public double r(double max) {
        return rand.nextDouble() * max;
    }

    public double r(double min, double max) {
        if (max < min) {
            return min + rand.nextDouble() * (min - max);
        } else return rand.nextDouble() * (max - min) + min;
    }

    public int i(int max) {
        return rand.nextInt(max);
    }

    public int i(int min, int max) {
        if (max < min) {
            return min + rand.nextInt(min - max);
        } else return rand.nextInt(max - min) + min;
    }

    public boolean chance(double $chance) {
        return rand.nextDouble() < $chance;
    }

    public int sign() {
        return rand.nextBoolean() ? 1 : -1;
    }

    public int sign(int num) {
        return rand.nextBoolean() ? num : -num;
    }

    public double sign(double num) {
        return rand.nextBoolean() ? num : -num;
    }

    public <T> T item(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        } else if (items.length == 1) {
            return items[0];
        } else {
            return items[rand.nextInt(items.length)];
        }
    }

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
