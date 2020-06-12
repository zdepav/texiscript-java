package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that represents a variable reference. */
public class StVariable extends StGenerator {

    /** variable name */
    private final String name;

    public StVariable(CodePosition pos, String name) {
        super(pos);
        this.name = name;
    }

    /** Gets variable name. */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "$" + name;
    }
}
