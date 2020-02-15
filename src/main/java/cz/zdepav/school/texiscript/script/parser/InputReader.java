package cz.zdepav.school.texiscript.script.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/** @author Zdenek Pavlatka */
public class InputReader {

    private final Reader input;

    private int buffer, position, line, lastLineColumn, column;

    public InputReader(InputStream input) {
        this.input = new BufferedReader(new InputStreamReader(input));
        buffer = -1;
        position = -1;
        line = 1;
        lastLineColumn = column = 0;
    }

    public boolean hasNext() {
        return peek() >= 0;
    }

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
        lastLineColumn = column;
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

    /** returns the position of the last read character */
    public CodePosition getCodePosition() {
        if (position < 0) {
            return new CodePosition(0, 0, 0, 0);
        } else if (column <= 0) {
            return new CodePosition(line - 1, lastLineColumn, position, 1);
        } else {
            return new CodePosition(line, column, position, 1);
        }
    }
}
