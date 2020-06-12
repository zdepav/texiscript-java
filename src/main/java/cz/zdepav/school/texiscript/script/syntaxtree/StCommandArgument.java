package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that can be an argument for a command. */
public abstract class StCommandArgument extends StNode {

    public StCommandArgument(CodePosition pos) {
        super(pos);
    }
}
