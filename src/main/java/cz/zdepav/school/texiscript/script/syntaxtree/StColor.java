package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** A node in the syntactic tree that represents a color. */
public class StColor extends StGenerator {

    /** value of this node */
    private final RgbaColor value;

    public StColor(CodePosition pos, RgbaColor value) {
        super(pos);
        this.value = value;
    }

    /** Gets the value of this node. */
    public RgbaColor getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
