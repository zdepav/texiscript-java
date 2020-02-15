package cz.zdepav.school.texiscript.generators.video;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class TimeGenerator extends Generator {

    private static double time = 0;

    public TimeGenerator(CodePosition pos) { }

    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(time);
    }

    @Override
    public double getDouble(double x, double y) {
        return time;
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.lerp(min, max, time);
    }

    public static double getGlobalTime() {
        return time;
    }

    public static void setGlobalTime(double time) {
        TimeGenerator.time = Utils.clamp(time, 0.0, 1.0);
    }

    @Override
    public void init(int outputSize, boolean randomize) { }
}
