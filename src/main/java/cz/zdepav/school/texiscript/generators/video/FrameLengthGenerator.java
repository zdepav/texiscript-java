package cz.zdepav.school.texiscript.generators.video;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Returns video frame length. */
public class FrameLengthGenerator extends Generator {

    private static double length = 0;

    public FrameLengthGenerator(CodePosition pos) { }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(length);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return length;
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, length);
    }

    public static double getGlobalLength() {
        return length;
    }

    public static void setGlobalLength(double time) {
        FrameLengthGenerator.length = Utils.clamp(time, 0.0, 1.0);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) { }
}
