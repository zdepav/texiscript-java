package cz.zdepav.school.texiscript.utils;

/** @author Zdenek Pavlatka */
public class Angle {

    private static double rad2deg, deg2rad;

    public static double
        deg10, deg15, deg18, deg20, deg30, deg36, deg45, deg60, deg72, deg90,
        deg120, deg135, deg150, deg180,
        deg210, deg225, deg240, deg270,
        deg300, deg315, deg330, deg360;

    public static double deg(double degrees) {
        return degrees * deg2rad;
    }

    public static double toDegrees(double radians) {
        return radians * rad2deg;
    }

    public static double rand() {
        return Rand.INSTANCE.r(deg360);
    }

    public static double rand(RandomGenerator rand) {
        return rand.r(deg360);
    }

    public static double wrap(double angle) {
        return (angle < 0 ? (deg360 + angle % deg360) : angle) % deg360;
    }

    public static double difference(double angle1, double angle2) {
        angle1 = wrap(angle1);
        angle2 = wrap(angle2);
        var diff = Math.abs(angle2 - angle1);
        if (diff <= deg180) {
            return angle1 < angle2 ? diff : -diff;
        } else {
            diff = (deg360 - diff) % deg360;
            return angle1 < angle2 ? -diff : diff;
        }
    }

    public static double absDifference(double angle1, double angle2) {
        angle1 = wrap(angle1);
        angle2 = wrap(angle2);
        var diff = Math.abs(angle2 - angle1);
        if (diff <= deg180) {
            return diff;
        } else {
            return (deg360 - diff) % deg360;
        }
    }

    public static double between(double angle1, double angle2) {
        angle1 = wrap(angle1);
        angle2 = wrap(angle2);
        var diff = Math.abs(angle2 - angle1);
        if (diff <= deg180) {
            return (angle1 + angle2) / 2;
        } else {
            return ((angle1 + angle2) / 2 + deg180) % deg360;
        }
    }

    static {
        rad2deg = 180 / Math.PI;
        deg2rad = Math.PI / 180;
        deg10 = Math.PI / 18;
        deg15 = Math.PI / 12;
        deg18 = Math.PI / 10;
        deg20 = Math.PI / 9;
        deg30 = Math.PI / 6;
        deg36 = Math.PI / 5;
        deg45 = Math.PI / 4;
        deg60 = Math.PI / 3;
        deg72 = Math.PI / 2.5;
        deg90 = Math.PI / 2;
        deg120 = Math.PI * 2 / 3;
        deg135 = Math.PI * 0.75;
        deg150 = Math.PI * 5 / 6;
        deg180 = Math.PI;
        deg210 = Math.PI * 7 / 6;
        deg225 = Math.PI * 1.25;
        deg240 = Math.PI * 4 / 3;
        deg270 = Math.PI * 1.5;
        deg300 = Math.PI * 5 / 3;
        deg315 = Math.PI * 1.75;
        deg330 = Math.PI * 11 / 6;
        deg360 = Math.PI * 2;
    }
}
