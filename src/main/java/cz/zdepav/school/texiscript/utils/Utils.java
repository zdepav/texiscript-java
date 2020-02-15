package cz.zdepav.school.texiscript.utils;

/** @author Zdenek Pavlatka */
public class Utils {

    private static String hex = "0123456789abcdef";

    /**
     * @param min min value (inclusive)
     * @param max max value (inclusive)
     */
    public static int clamp(int value, int min, int max) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * @param min min value (inclusive)
     * @param max max value (inclusive)
     */
    public static double clamp(double value, double min, double max) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * @param max max value (exclusive)
     */
    public static int wrap(int value, int max) {
        if (value < 0) {
            value = max - (-value) % max;
        }
        return value % max;
    }

    /**
     * @param min min value (inclusive)
     * @param max max value (exclusive)
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
     * @param max max value (exclusive)
     */
    public static double wrap(double value, double max) {
        if (value < 0) {
            value = max - (-value) % max;
        }
        return value % max;
    }

    public static double wrap(double value) {
        if (value < 0) {
            value = 1 - (-value) % 1;
        }
        return value % 1;
    }

    /**
     * @param min min value (inclusive)
     * @param max max value (exclusive)
     */
    public static double wrap(double value, double min, double max) {
        value -= min;
        var range = max - min;
        if (value < 0) {
            value = range - (-value) % range;
        }
        return value % range + min;
    }

    public static double lerp(double f1, double f2, double ammount) {
        if (ammount <= 0) {
            return f1;
        } else if (ammount >= 1) {
            return f2;
        } else {
            return f1 + ammount * (f2 - f1);
        }
    }

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

    public static double interpolateSmooth(double f1, double f2, double ammount) {
        if (ammount <= 0) {
            return f1;
        } else if (ammount >= 1) {
            return f2;
        } else {
            return f1 + (1 - Math.cos(ammount * Math.PI)) * 0.5 * (f2 - f1);
        }
    }

    public static int flatten(int width, int x, int y) {
        return width * y + x;
    }

    /**
     * @param steps number of values between 0 and 1
     */
    public static double granulate(double value, double steps) {
        return Math.floor(value * steps) / steps + 1 / steps / 2;
    }

    public static String byteToHex(int $byte) {
        $byte = clamp($byte, 0, 255);
        return "" + hex.charAt($byte / 16) + hex.charAt($byte % 16);
    }

    public static double sqr(double value) {
        return value * value;
    }

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
}
