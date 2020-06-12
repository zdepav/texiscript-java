package cz.zdepav.school.texiscript.generators.cellular;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Metric;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Generates pattern resembling cobbles. */
public class CobbleCellularGenerator extends CellularGenerator {

    public CobbleCellularGenerator(
        double density, Metric metric,
        Generator color1, Generator color2,
        double chaos, Generator curve
    ) {
        super(density, metric, color1, color2, chaos, curve);
    }

    /** {@inheritDoc} */
    @Override
    protected double getDistance(double x, double y, double[] distancesTo2Nearest) {
        return distancesTo2Nearest[1] - distancesTo2Nearest[0];
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return color1.getColor(x, y).lerp(
            color2.getColor(x, y),
            curve.getDouble(getDistance(x, y) / maxDist, y)
        );
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return Utils.lerp(
            color1.getDouble(x, y),
            color2.getDouble(x, y),
            curve.getDouble(getDistance(x, y) / maxDist, y)
        );
    }
}
