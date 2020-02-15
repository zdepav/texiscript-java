package cz.zdepav.school.texiscript.script.parser;

import cz.zdepav.school.texiscript.utils.RgbaColor;

import java.io.InputStream;
import java.util.regex.Pattern;

/** @author Zdenek Pavlatka */
public class Lexer implements TokenSource {

    private final InputReader input;
    private Token buffer;

    public Lexer(InputStream input) {
        this.input = new InputReader(input);
        buffer = null;
    }

    @Override
    public Token peekToken() throws SyntaxException {
        if (buffer != null) {
            return buffer;
        } else {
            return buffer = parseNextToken();
        }
    }

    @Override
    public Token readToken() throws SyntaxException {
        if (buffer != null) {
            Token ret = buffer;
            buffer = null;
            return ret;
        } else {
            return parseNextToken();
        }
    }

    @Override
    public void skipToken() throws SyntaxException {
        if (buffer != null) {
            buffer = null;
        } else {
            parseNextToken();
        }
    }

    @Override
    public CodePosition getCodePosition() {
        return input.getCodePosition();
    }

    private SyntaxException error(String message) {
        return new SyntaxException(input.getCodePosition(), message);
    }

    private Token parseNextToken() throws SyntaxException {
        if (!input.hasNext()) {
            return null;
        }
        while (input.hasNext()) {
            char c = (char)input.read();
            switch (c) {
                case '(':
                    return Token.lpar(input.getCodePosition());
                case ')':
                    return Token.rpar(input.getCodePosition());
                case '=':
                    return Token.assignment(input.getCodePosition());
                case ',':
                    return Token.comma(input.getCodePosition());
                case '$':
                    return readVariableToken();
                case '"':
                    return readStringToken();
                case '#':
                    return readColorToken();
                case ':':
                    return readSeedToken();
                case '/':
                    if (input.peek() == '/') {
                        skipRestOfLine();
                        continue;
                    } else if (input.peek() == '*') {
                        input.read();
                        skipRestOfComment();
                        continue;
                    } else throw error("Unexpected character '/'");
                case '\n':
                    continue;
                default:
                    if (Character.isUpperCase(c)) {
                        return readCommandToken(c);
                    } else if (Character.isLowerCase(c)) {
                        return readFunctionToken(c);
                    } else if (c == '-' || c == '+' || (c >= '0' && c <= '9')) {
                        return readNumberToken(c);
                    } else if (!Character.isWhitespace(c)) {
                        throw error("Invalid character '" + c + "'");
                    }
            }
        }
        return null;
    }

    private void skipRestOfComment() {
        int c;
        while ((c = input.peek()) > 0) {
            input.read();
            if (c == '*' && input.peek() == '/') {
                input.read();
                return;
            }
        }
    }

    private void skipRestOfLine() {
        int c;
        while ((c = input.peek()) > 0 && c != '\n') {
            input.read();
        }
    }

    private boolean isValidVariableNameCharacter(int c) {
        return c == '_' ||
            (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            (c >= '0' && c <= '9');
    }

    private boolean isValidHexCharacter(int c) {
        return (c >= 'a' && c <= 'f') ||
            (c >= 'A' && c <= 'F') ||
            (c >= '0' && c <= '9');
    }

    private Token readVariableToken() throws SyntaxException {
        var posA = input.getCodePosition();
        var buffer = new StringBuilder();
        int c;
        var prevWasDot = true;
        while ((c = input.peek()) > 0) {
            if (c == '.') {
                if (prevWasDot) {
                    input.read();
                    throw error("Invalid character '.'");
                } else {
                    prevWasDot = true;
                    buffer.append((char)input.read());
                }
            } else if (isValidVariableNameCharacter(c)) {
                buffer.append((char)input.read());
                prevWasDot = false;
            } else break;
        }
        if (prevWasDot) {
            throw error("Invalid character '.'");
        }
        var name = buffer.toString();
        if (name.matches("^([a-z]+\\.)*[a-zA-Z0-9_]+$")) {
            return Token.variable(posA.until(input.getCodePosition()), name);
        } else {
            throw error("Invalid package name '" + name.substring(0, name.lastIndexOf('.')) + '\'');
        }
    }

    private Token readSeedToken() {
        var posA = input.getCodePosition();
        int seed = 0;
        int c;
        while ((c = input.peek()) > 0 && c >= '0' && c <= '9') {
            seed = input.read() - '0' + seed * 10;
        }
        return Token.seed(posA.until(input.getCodePosition()), seed);
    }

    private Token readCommandToken(char firstChar) {
        var posA = input.getCodePosition();
        var buffer = new StringBuilder();
        buffer.append(firstChar);
        int c;
        while ((c = input.peek()) > 0 && Character.isLowerCase(c)) {
            buffer.append((char)input.read());
        }
        return Token.command(posA.until(input.getCodePosition()), buffer.toString());
    }

    private Token readFunctionToken(char firstChar) throws SyntaxException {
        var posA = input.getCodePosition();
        var buffer = new StringBuilder();
        buffer.append(firstChar);
        var prevWasDot = false;
        int c;
        while ((c = input.peek()) > 0) {
            if (c == '.') {
                if (prevWasDot) {
                    throw error("Invalid character '.'");
                } else {
                    prevWasDot = true;
                    buffer.append((char)input.read());
                }
            } else if (Character.isLowerCase(c)) {
                buffer.append((char)input.read());
                prevWasDot = false;
            } else break;
        }
        if (prevWasDot) {
            throw error("Invalid character '.'");
        }
        return Token.function(posA.until(input.getCodePosition()), buffer.toString());
    }

    private Token readStringToken() throws SyntaxException {
        var posA = input.getCodePosition();
        var buffer = new StringBuilder();
        int c;
        while ((c = input.peek()) > 0) {
            input.read();
            if (c == '"') {
                return Token.string(posA.until(input.getCodePosition()), buffer.toString());
            } else if (c == '\\') {
                if (input.peek() < 0) {
                    throw error("Unexpected end of input");
                }
                buffer.append((char)input.read());
            } else {
                buffer.append((char)c);
            }
        }
        throw error("Unexpected end of input");
    }

    private Token readColorToken() throws SyntaxException {
        var posA = input.getCodePosition();
        var buffer = new StringBuilder("#");
        int c;
        while ((c = input.peek()) > 0 && isValidHexCharacter(c)) {
            buffer.append((char)input.read());
        }
        try {
            return Token.color(posA.until(input.getCodePosition()), RgbaColor.parseHex(buffer.toString()));
        } catch (IllegalArgumentException ex) {
            throw new SyntaxException(posA.until(input.getCodePosition()), ex);
        }
    }

    private void readIntegerString(StringBuilder buffer, char firstChar, boolean allowSign) throws SyntaxException {
        if (firstChar == '-' || firstChar == '+') {
            if (!allowSign) {
                throw error("Unexpected character '" + firstChar + "'");
            }
        } else if (firstChar < '0' || firstChar > '9') {
            throw error("Unexpected character '" + firstChar + "'");
        }
        buffer.append(firstChar);
        int c;
        while ((c = input.peek()) > 0 && c >= '0' && c <= '9') {
            buffer.append((char)input.read());
        }
    }

    private Token readNumberToken(char firstChar) throws SyntaxException {
        var posA = input.getCodePosition();
        var buffer = new StringBuilder();
        readIntegerString(buffer, firstChar, true);
        if (input.peek() == '.') {
            buffer.append((char)input.read());
            if (input.peek() < 0) {
                throw error("Unexpected end of input");
            }
            readIntegerString(buffer, (char)input.read(), false);
        }
        int c = input.peek();
        if (c == 'e' || c == 'E') {
            buffer.append((char)input.read());
            if (input.peek() < 0) {
                throw error("Unexpected end of input");
            }
            readIntegerString(buffer, (char)input.read(), true);
        }
        return Token.number(posA.until(input.getCodePosition()), Double.parseDouble(buffer.toString()));
    }
}
