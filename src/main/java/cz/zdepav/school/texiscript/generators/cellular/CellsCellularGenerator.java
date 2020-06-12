package cz.zdepav.school.texiscript.generators.cellular;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.Metric;

/** Generates pattern resembling organic cells. */
public class CellsCellularGenerator extends CellularGenerator {

    public CellsCellularGenerator(
        double density, Metric metric,
        Generator color1, Generator color2,
        double chaos, Generator curve
    ) {
        super(density, metric, color1, color2, chaos, curve);
    }

    /** {@inheritDoc} */
    @Override
    protected double getDistance(double x, double y, double[] distancesTo2Nearest) {
        return distancesTo2Nearest[0] * distancesTo2Nearest[0];
    }
}
