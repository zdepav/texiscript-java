package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that represents a number. */
public class StNumber extends StGenerator {

    /** value of this node */
    private final double value;

    public StNumber(CodePosition pos, double value) {
        super(pos);
        this.value = value;
    }

    /** Gets the value of this node. */
    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }
}
