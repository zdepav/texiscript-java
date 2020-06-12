package cz.zdepav.school.texiscript.utils;

/** Utility class with some usefull methods. */
public class Utils {

    /** lowercase hexadecimal digits */
    private static final String hex = "0123456789abcdef";

    /**
     * Clamps a value between given bounds.
     * @param value value to clamp
     * @param min min value (inclusive)
     * @param max max value (inclusive)
     * @return Given value if it is between min and max, min if it is lower than min, max otherwise.
     */
    public static int clamp(int value, int min, int max) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * Clamps a value between given bounds.
     * @param value value to clamp
     * @param min min value (inclusive)
     * @param max max value (inclusive)
     * @return Given value if it is between min and max, min if it is lower than min, max otherwise.
     */
    public static double clamp(double value, double min, double max) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * Wraps a given value in an interval [0;max).
     * @param value value to wrap
     * @param max max value (exclusive)
     * @return A number between 0 (inclusive) and max (exclusive), that can be computed from given value by repeatedly
     *         adding or subtracting max.
     */
    public static int wrap(int value, int max) {
        if (value < 0) {
            value = max - (-value) % max;
        }
        return value % max;
    }

    /**
     * Wraps a given value in an interval [min;max).
     * @param value value to wrap
     * @param min min value (inclusive)
     * @param max max value (exclusive)
     * @return A number between min (inclusive) and max (exclusive), that can be computed from given value by repeatedly
     *         adding or subtracting (max - min).
     */
    public static int wrap(int value, int min, int max) {
        value -= min;
        var range = max - min;
        if (value < 0) {
            value = range - (-value) % range;
        }
        return value % range + min;
    }

    /**
     * Wraps a given value in an interval [0;max).
     * @param value value to wrap
     * @param max max value (exclusive)
     * @return A number between 0 (inclusive) and max (exclusive), that can be computed from given value by repeatedly
     *         adding or subtracting max.
     */
    public static double wrap(double value, double max) {
        if (value < 0) {
            value = max - (-value) % max;
        }
        return value % max;
    }

    /**
     * Wraps a given value in an interval [0;1).
     * @param value value to wrap
     * @return A number between 0 (inclusive) and 1 (exclusive), that can be computed from given value by adding or
     *         subtracting an integer.
     */
    public static double wrap(double value) {
        if (value < 0) {
            value = 1 - (-value) % 1;
        }
        return value % 1;
    }

    /**
     * Wraps a given value in an interval [min;max).
     * @param value value to wrap
     * @param min min value (inclusive)
     * @param max max value (exclusive)
     * @return A number between min (inclusive) and max (exclusive), that can be computed from given value by repeatedly
     *         adding or subtracting (max - min).
     */
    public static double wrap(double value, double min, double max) {
        value -= min;
        var range = max - min;
        if (value < 0) {
            value = range - (-value) % range;
        }
        return value % range + min;
    }

    /**
     * Interpolates between two values using linear interpolation.
     * @param f1 first value
     * @param f2 second value
     * @param ammount interpolation ammount between the two colors (0 ~ f1, 1 ~ f2)
     * @return Resulting value between f1 and f2.
     */
    public static double lerp(double f1, double f2, double ammount) {
        if (ammount <= 0) {
            return f1;
        } else if (ammount >= 1) {
            return f2;
        } else {
            return f1 + ammount * (f2 - f1);
        }
    }

    /**
     * Interpolates between two integer values using linear interpolation.
     * @param f1 first value
     * @param f2 second value
     * @param ammount interpolation ammount between the two colors (0 ~ f1, 1 ~ f2)
     * @return Resulting integer value between f1 and f2.
     */
    public static int lerpInt(int f1, int f2, double ammount) {
        if (ammount <= 0) {
            return f1;
        } else if (ammount >= 1) {
            return f2;
        } else {
            var f = (int)((Math.abs(f1 - f2) + 1) * ammount);
            return f1 < f2 ? f1 + f : f1 - f;
        }
    }

    /**
     * Interpolates between two values using non-linear interpolation to create a smooth transition.
     * @param f1 first value
     * @param f2 second value
     * @param ammount interpolation ammount between the two colors (0 ~ f1, 1 ~ f2)
     * @return Resulting value between f1 and f2.
     */
    public static double interpolateSmooth(double f1, double f2, double ammount) {
        if (ammount <= 0) {
            return f1;
        } else if (ammount >= 1) {
            return f2;
        } else {
            return f1 + ammount * ammount * (3.0 - 2.0 * ammount) * (f2 - f1);
        }
    }

    /**
     * Computes 1D array index from 2D grid coordinates.
     * @param width 2D grid width
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @return Computed index.
     */
    public static int flatten(int width, int x, int y) {
        return width * y + x;
    }

    /**
     * Rounds the value to a nearest step between 0 and 1.
     * @param value number to round
     * @param steps number of values between 0 and 1
     */
    public static double granulate(double value, double steps) {
        return Math.floor(value * steps) / steps + 0.5 / steps;
    }

    /** Converts a byte value to a two-digit hexadecimal string. */
    public static String byteToHex(int $byte) {
        $byte = clamp($byte, 0, 255);
        return "" + hex.charAt($byte / 16) + hex.charAt($byte % 16);
    }

    /** Computes second power of a given value. */
    public static double sqr(double value) {
        return value * value;
    }

    /** Creates an ordinal string representation of a number. */
    public static String ordinal(int value) {
        switch (Math.abs(value)) {
            case 1:
                return value + "st";
            case 2:
                return value + "nd";
            case 3:
                return value + "rd";
            default:
                return value + "th";
        }
    }

    /**
     * Converts time in milliseconds to a human-readable format.
     * @param millis time in milliseconds
     * @return formatted string describing the given time
     */
    public static String FormattedCurrentTime(long millis) {
        var hours = millis / 360000;
        var minutes = millis / 60000 % 60;
        var seconds = millis / 1000 % 60;
        var miliseconds = millis % 1000;
        var timeString = new StringBuilder();
        if (hours > 0) {
            timeString.append(hours);
            timeString.append("h ");
        }
        if (minutes > 0) {
            timeString.append(minutes);
            timeString.append("m ");
        }
        if (seconds > 0) {
            timeString.append(seconds);
            timeString.append("s ");
        }
        if (miliseconds > 0 || timeString.length() == 0) {
            timeString.append(miliseconds);
            timeString.append("ms");
        }
        return timeString.toString();
    }
}
