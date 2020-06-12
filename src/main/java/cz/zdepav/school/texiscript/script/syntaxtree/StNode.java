package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree. */
public abstract class StNode {

    /** position of this node in the source code */
    private final CodePosition pos;

    protected StNode(CodePosition pos) {
        this.pos = pos;
    }

    /** Gets position of this node in the source code. */
    public CodePosition getCodePosition() {
        return pos;
    }
}
