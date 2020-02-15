package cz.zdepav.school.texiscript.utils;

/** @author Zdenek Pavlatka */
@FunctionalInterface
public interface Curve {

    double at(double x);

    Curve linear = x -> x;
    Curve arc = x -> Math.sqrt(x * (2 - x));
    Curve invArc = x -> 1 - Math.sqrt(1 - x * x);
    Curve sqr = x -> x * x;
    Curve sqrt = Math::sqrt;
    Curve log = x -> Math.log10(1 + x * 9);
    Curve sin = x -> (1 - Math.cos(x * Math.PI)) * 0.5;
}
