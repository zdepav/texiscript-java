package cz.zdepav.school.texiscript.script.interpreter;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class SemanticException extends Exception {

    private final CodePosition pos;

    public SemanticException(CodePosition pos, String message) {
        super(message);
        this.pos = pos;
    }

    public SemanticException(CodePosition pos, Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.pos = pos;
    }

    public CodePosition getCodePosition() {
        return pos;
    }
}
