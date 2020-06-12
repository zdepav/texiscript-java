package cz.zdepav.school.texiscript.script.interpreter;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** Exception thrown when a script contains a semantic error. */
public class SemanticException extends Exception {

    /** position of the error in the script */
    private final CodePosition pos;

    /**
     * Constructs SemanticException using a position and error message.
     * @param pos position in the script
     * @param message error message
     */
    public SemanticException(CodePosition pos, String message) {
        super(message);
        this.pos = pos;
    }

    /**
     * Constructs SemanticException using a position and an underlying exception.
     * @param pos position in the script
     * @param throwable underlying exception
     */
    public SemanticException(CodePosition pos, Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.pos = pos;
    }

    /** Gets the position of the error in the script. */
    public CodePosition getCodePosition() {
        return pos;
    }
}
