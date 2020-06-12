package cz.zdepav.school.texiscript.script.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/** Class for InputStream reading that allows peeking and normalizes line ends to \"\\n\". */
public class InputReader {

    /** underlying buffered reader */
    private final Reader input;

    /** buffer for peeked character */
    private int buffer;

    /** current position in stream */
    private int position;

    /** current line number */
    private int line;

    /** previous line length */
    private int prevLineLength;

    /** position in current line */
    private int column;

    /** Constructs a reader from an input stream. */
    public InputReader(InputStream input) {
        this.input = new BufferedReader(new InputStreamReader(input));
        buffer = -1;
        position = -1;
        line = 1;
        prevLineLength = column = 0;
    }

    /** Returns true if EOF was not reached yet, false otherwise. */
    public boolean hasNext() {
        return peek() >= 0;
    }

    /**
     * Gets the next character without advancing position or -1 if EOF was reached. Replaces line ends with a single
     * \"\\n\" character.
     */
    public int peek() {
        if (buffer < 0) {
            try {
                buffer = input.read();
            } catch (IOException e) {
                return -1;
            }
        }
        return buffer == '\r' ? '\n' : buffer;
    }

    /**
     * Gets the next character and advances position. Returns -1 instead if EOF was reached, replaces line ends with a
     * single \"\\n\" character.
     */
    public int read() {
        int ret;
        if (buffer >= 0) {
            ret = buffer;
            buffer = -1;
        } else {
            try {
                ret = input.read();
            } catch (IOException e) {
                return -1;
            }
        }
        ++column;
        prevLineLength = column;
        ++position;
        if (ret == '\n') {
            ++line;
            column = 0;
        } else if (ret == '\r') {
            if (peek() == '\n') {
                ++position;
                buffer = -1;
            }
            ret = '\n';
            ++line;
            column = 0;
        }
        return ret;
    }

    /** Gets the position of the last read character. */
    public CodePosition getCodePosition() {
        if (position < 0) {
            return new CodePosition(0, 0, 0, 0);
        } else if (column <= 0) {
            return new CodePosition(line - 1, prevLineLength, position, 1);
        } else {
            return new CodePosition(line, column, position, 1);
        }
    }
}
