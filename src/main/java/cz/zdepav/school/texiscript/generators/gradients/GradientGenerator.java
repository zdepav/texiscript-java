package cz.zdepav.school.texiscript.generators.gradients;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/** Base class for gradient generators. */
public abstract class GradientGenerator extends Generator {

    private final List<GradientStopGenerator> colorStops;

    public GradientGenerator() {
        colorStops = new ArrayList<>();
    }

    /** Adds a position-color pair to the gradient. */
    public void addColorStop(GradientStopGenerator stop) {
        if (colorStops.size() == 0 || stop.pos >= colorStops.get(colorStops.size() - 1).pos) {
            colorStops.add(stop);
        } else {
            int i = colorStops.size() - 1;
            while (i > 0 && stop.pos < colorStops.get(i - 1).pos) --i;
            colorStops.add(i, stop);
        }
    }

    /** Converts coordinates to a position in the gradient. */
    protected abstract double getPosition(double x, double y);

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        var position = getPosition(x, y);
        if (colorStops.size() == 0) {
            return RgbaColor.black;
        } else if (colorStops.size() == 1 || position <= colorStops.get(0).pos) {
            return colorStops.get(0).getColor(x, y);
        } else if (position >= colorStops.get(colorStops.size() - 1).pos) {
            return colorStops.get(colorStops.size() - 1).getColor(x, y);
        } else {
            int i = 1;
            while (position > colorStops.get(i).pos) ++i;
            return colorStops.get(i - 1).getColor(x, y).lerp(
                colorStops.get(i).getColor(x, y),
                (position - colorStops.get(i - 1).pos) / (colorStops.get(i).pos - colorStops.get(i - 1).pos)
            );
        }
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        var position = getPosition(x, y);
        if (colorStops.size() == 0) {
            return 0.0;
        } else if (colorStops.size() == 1 || position <= colorStops.get(0).pos) {
            return colorStops.get(0).getDouble(x, y);
        } else if (position >= colorStops.get(colorStops.size() - 1).pos) {
            return colorStops.get(colorStops.size() - 1).getDouble(x, y);
        } else {
            int i = 1;
            while (position > colorStops.get(i).pos) ++i;
            return Utils.lerp(
                colorStops.get(i - 1).getDouble(x, y),
                colorStops.get(i).getDouble(x, y),
                (position - colorStops.get(i - 1).pos) / (colorStops.get(i).pos - colorStops.get(i - 1).pos)
            );
        }
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        for (var stop: colorStops) {
            stop.init(outputSize, randomize);
        }
    }
}
