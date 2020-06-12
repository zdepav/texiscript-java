package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that represents a string. */
public class StString extends StCommandArgument {

    /** value of this node */
    private final String value;

    public StString(CodePosition pos, String value) {
        super(pos);
        this.value = value;
    }

    /** Gets the value of this node. */
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return '"' + value + '"';
    }
}
