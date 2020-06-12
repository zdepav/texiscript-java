package cz.zdepav.school.texiscript.script.parser;

/** Exception thrown when a script contains a syntax error. */
public class SyntaxException extends Exception {

    /** position of the error in the script */
    private final CodePosition pos;

    /**
     * Constructs SyntaxException using a position and error message.
     * @param pos position in the script
     * @param message error message
     */
    public SyntaxException(CodePosition pos, String message) {
        super(message);
        this.pos = pos;
    }

    /**
     * Constructs SyntaxException using a position and error message.
     * @param pos position in the script
     * @param throwable underlying exception
     */
    public SyntaxException(CodePosition pos, Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.pos = pos;
    }

    /** Gets the position of the error in the script. */
    public CodePosition getCodePosition() { return pos; }
}
