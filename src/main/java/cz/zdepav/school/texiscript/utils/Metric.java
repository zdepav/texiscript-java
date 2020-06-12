package cz.zdepav.school.texiscript.utils;

/**
 * Interface for distance computation.
 * Contains implementations for some metrics.
 */
@FunctionalInterface
public interface Metric {

    /**
     * Computes distance from 0;0 to dx;dy.
     * @param dx horizontal distance
     * @param dy vertical distance
     * @return distance to the given point
     */
    double distance(double dx, double dy);

    /** Euclidean distance (equal to minkowski distance with p = 2) */
    Metric euclidean = (dx, dy) ->
        Math.sqrt(dx * dx + dy * dy);

    /** Manhattan distance (equal to minkowski distance with p = 1) */
    Metric manhattan = (dx, dy) ->
        Math.abs(dx) + Math.abs(dy);

    /** Chebyshev (chessboard) distance */
    Metric chebyshev = (dx, dy) ->
        Math.max(Math.abs(dx), Math.abs(dy));

    /** Minkowski distance with p = 0.5 */
    Metric minkowski = (dx, dy) ->
        Utils.sqr(Math.sqrt(Math.abs(dx)) + Math.sqrt(Math.abs(dy)));
}
