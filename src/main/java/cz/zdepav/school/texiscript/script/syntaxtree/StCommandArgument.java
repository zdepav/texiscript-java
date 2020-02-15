package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public abstract class StCommandArgument extends StNode {

    public StCommandArgument(CodePosition pos) {
        super(pos);
    }
}
