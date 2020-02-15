package cz.zdepav.school.texiscript.script.parser;

/** @author Zdenek Pavlatka */
public class SyntaxException extends Exception {

    private final CodePosition pos;

    public SyntaxException(CodePosition pos, String message) {
        super(message);
        this.pos = pos;
    }

    public SyntaxException(CodePosition pos, Throwable throwable) {
        super(throwable.getMessage(), throwable);
        this.pos = pos;
    }

    public CodePosition getCodePosition() { return pos; }
}
