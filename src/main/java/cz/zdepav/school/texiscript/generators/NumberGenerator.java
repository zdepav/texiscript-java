package cz.zdepav.school.texiscript.generators;

import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class NumberGenerator extends Generator {

    private double number;

    NumberGenerator(double number) {
        this.number = number;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        return new RgbaColor(number);
    }

    @Override
    public double getDouble(double x, double y) {
        return number;
    }

    @Override
    public double getDouble(double x, double y, double min, double max) {
        return Utils.clamp(number, min, max);
    }

    public boolean isNumber() {
        return true;
    }

    @Override
    public void init(int outputSize, boolean randomize) { }
}
