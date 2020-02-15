package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class StVariable extends StGenerator {

    private final String name;

    public StVariable(CodePosition pos, String name) {
        super(pos);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "$" + name;
    }
}
