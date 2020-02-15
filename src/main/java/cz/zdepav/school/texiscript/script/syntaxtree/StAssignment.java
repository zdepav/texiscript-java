package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class StAssignment extends StNode {

    private final String variableName;
    private final StGenerator value;

    public StAssignment(CodePosition pos, String variableName, StGenerator value) {
        super(pos);
        this.variableName = variableName;
        this.value = value;
    }

    public String getVariableName() {
        return variableName;
    }

    public StGenerator getValue() {
        return value;
    }

    @Override
    public String toString() {
        return '$' + variableName + " = " + value.toString() + "\n";
    }
}
