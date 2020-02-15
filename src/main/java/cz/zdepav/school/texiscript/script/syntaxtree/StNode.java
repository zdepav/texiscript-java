package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public abstract class StNode {

    private CodePosition pos;

    protected StNode(CodePosition pos) {
        this.pos = pos;
    }

    public CodePosition getCodePosition() {
        return pos;
    }
}
