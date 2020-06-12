package cz.zdepav.school.texiscript.utils;

/**
 * Interface for curves (functions of x for 0 <= x <= 1).
 * Contains implementations of some basic curves.
 */
@FunctionalInterface
public interface Curve {

    /**
     * Computes function value for given x.
     * @param x position on x-axis
     * @return function value at the given position
     */
    double at(double x);

    /** linear function (y = x) */
    Curve linear = x -> x;

    /** circle segment with center in x = 1, y = 0 */
    Curve arc = x -> Math.sqrt(x * (2 - x));

    /** circle segment with center in x = 0, y = 1 */
    Curve invArc = x -> 1 - Math.sqrt(1 - x * x);

    /** combination of arc and invArc (invArc goes first) */
    Curve doubleArc = x ->
        x < 0.5
            ? invArc.at(x * 2) * 0.5
            : arc.at(x * 2 - 1) * 0.5 + 0.5;

    /** combination of arc and invArc (arc goes first) */
    Curve invDoubleArc = x ->
        x < 0.5
            ? arc.at(x * 2) * 0.5
            : invArc.at(x * 2 - 1) * 0.5 + 0.5;

    /** second power (y = second power of x) */
    Curve sqr = x -> x * x;

    /** square root (y = square root of x) */
    Curve sqrt = Math::sqrt;

    /** logarithmic function (y = logarithm with base 10 of x mapped to interval [1;10]) */
    Curve log = x -> Math.log10(1 + x * 9);

    /** sinusoid (y = 0.5 - 0.5 * cosine of x mapped to interval [0;180°]) */
    Curve sin = x -> (1 - Math.cos(x * Math.PI)) * 0.5;
}
