package cz.zdepav.school.texiscript.script.parser;

/** Token source for parser that returns tokens from an array. */
public class ArrayTokenSource implements TokenSource {

    /** token sequence */
    private final Token[] tokens;

    /** current position in the token sequence */
    private int index;

    public ArrayTokenSource(Token[] tokens) {
        this.tokens = tokens == null ? new Token[0] : tokens;
        index = 0;
    }

    /** {@inheritDoc} */
    public Token peekToken() {
        return index + 1 < tokens.length ? tokens[index + 1] : null;
    }

    /** {@inheritDoc} */
    public Token readToken() {
        if (index + 1 < tokens.length) {
            ++index;
        }
        return index < tokens.length ? tokens[index] : null;
    }

    /** {@inheritDoc} */
    public void skipToken() {
        if (index + 1 < tokens.length) {
            ++index;
        }
    }

    /** {@inheritDoc} */
    public CodePosition getCodePosition() {
        return index > 0 && tokens.length > 0
            ? tokens[index - 1].getCodePosition()
            : new CodePosition(0, 0, 0, 0);
    }
}
