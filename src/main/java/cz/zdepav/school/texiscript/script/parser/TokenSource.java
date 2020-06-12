package cz.zdepav.school.texiscript.script.parser;

/** Token source for parser. */
public interface TokenSource {

    /**
     * Returns next token without advancing position. Returns null if EOF was reached.
     * @return next token
     * @throws SyntaxException When the input contains errors.
     */
    Token peekToken() throws SyntaxException;

    /**
     * Returns next token and advances position. Returns null if EOF was reached.
     * @return next token
     * @throws SyntaxException When the input contains errors.
     */
    Token readToken() throws SyntaxException;

    /**
     * Skips next token.
     * @throws SyntaxException When the input contains errors.
     */
    void skipToken() throws SyntaxException;

    /** Gets current position in the source code. */
    CodePosition getCodePosition();
}
