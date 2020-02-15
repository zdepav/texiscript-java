package cz.zdepav.school.texiscript.script.parser;

/** @author Zdenek Pavlatka */
public class CodePosition {

    private final int line, column, start, length;

    public CodePosition(int line, int column, int start, int length) {
        this.line = line;
        this.column = column;
        this.start = start;
        this.length = length;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public int getStart() {
        return start;
    }

    public int getLength() {
        return length;
    }

    public CodePosition until(CodePosition b) {
        return new CodePosition(line, column, start, b.start - start + b.length);
    }

    @Override
    public String toString() {
        return "line " + line + ", column " + column;
    }
}
