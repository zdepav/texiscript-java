package cz.zdepav.school.texiscript.script.parser;

/** @author Zdenek Pavlatka */
public interface TokenSource {

    Token peekToken() throws SyntaxException;

    Token readToken() throws SyntaxException;

    void skipToken() throws SyntaxException;

    CodePosition getCodePosition();
}
