package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that represents a variable assignment. */
public class StAssignment extends StNode {

    /** variable name */
    private final String variableName;

    /** variable value */
    private final StGenerator value;

    public StAssignment(CodePosition pos, String variableName, StGenerator value) {
        super(pos);
        this.variableName = variableName;
        this.value = value;
    }

    /** Gets variable name. */
    public String getVariableName() {
        return variableName;
    }

    /** Gets variable value. */
    public StGenerator getValue() {
        return value;
    }

    @Override
    public String toString() {
        return '$' + variableName + " = " + value.toString() + "\n";
    }
}
