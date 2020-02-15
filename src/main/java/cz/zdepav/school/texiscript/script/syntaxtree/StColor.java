package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class StColor extends StGenerator {

    private final RgbaColor value;

    public StColor(CodePosition pos, RgbaColor value) {
        super(pos);
        this.value = value;
    }

    public RgbaColor getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
