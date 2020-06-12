package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that represents any expression resulting in a generator. */
public class StGenerator extends StCommandArgument {

    public StGenerator(CodePosition pos) {
        super(pos);
    }
}
