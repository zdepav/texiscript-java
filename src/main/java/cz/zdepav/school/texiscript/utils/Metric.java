package cz.zdepav.school.texiscript.utils;

/** @author Zdenek Pavlatka */
@FunctionalInterface
public interface Metric {

    double distance(double dx, double dy);

    Metric euclidean = (dx, dy) -> Math.sqrt(dx * dx + dy * dy);
    Metric manhattan = (dx, dy) -> Math.abs(dx) + Math.abs(dy);
    Metric chebyshev = (dx, dy) -> Math.max(Math.abs(dx), Math.abs(dy));
    Metric minkowski = (dx, dy) -> Utils.sqr(Math.sqrt(Math.abs(dx)) + Math.sqrt(Math.abs(dy)));
}
