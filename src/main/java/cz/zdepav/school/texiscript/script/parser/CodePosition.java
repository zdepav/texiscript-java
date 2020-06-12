package cz.zdepav.school.texiscript.script.parser;

/** Repreents a position or code segment in a script. */
public class CodePosition {

    /** line number */
    private final int line;

    /** character index in the line */
    private final int column;

    /** starting character index in the file */
    private final int start;

    /** character count */
    private final int length;

    /**
     * Constructs a position or code segment
     * @param line line number
     * @param column character index in the line
     * @param start starting character index in the file
     * @param length character count
     */
    public CodePosition(int line, int column, int start, int length) {
        this.line = line;
        this.column = column;
        this.start = start;
        this.length = length;
    }

    /** Gets line number. */
    public int getLine() {
        return line;
    }

    /** Gets character index in the line. */
    public int getColumn() {
        return column;
    }

    /** Gets starting character index in the file. */
    public int getStart() {
        return start;
    }

    /** Gets character count. */
    public int getLength() {
        return length;
    }

    /**
     * Combines two positions/segments into one segment.
     * @param b second position
     * @return a code segment starting at this position's start and ending at the other position's end
     */
    public CodePosition until(CodePosition b) {
        return new CodePosition(line, column, start, b.start - start + b.length);
    }

    @Override
    public String toString() {
        return "line " + line + ", column " + column;
    }
}
