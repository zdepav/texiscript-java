package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class StString extends StCommandArgument {

    private final String value;

    public StString(CodePosition pos, String value) {
        super(pos);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return '"' + value + '"';
    }
}
