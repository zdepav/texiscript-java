package cz.zdepav.school.texiscript.script.parser;

import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class Token {

    private final CodePosition pos;
    private final int seed;
    private final double number;
    private final RgbaColor color;
    private final String string;
    private final TokenType type;

    private Token(CodePosition pos, TokenType type, RgbaColor color, String string, double number, int seed) {
        this.pos = pos;
        this.type = type;
        this.seed = seed;
        this.color = color;
        this.string = string;
        this.number = number;
    }

    private Token(TokenType type, CodePosition pos) {
        this(pos, type, null, null, 0, 0);
    }

    public CodePosition getCodePosition() {
        return pos;
    }

    double getNumber() {
        return number;
    }

    public int getSeed() {
        return seed;
    }

    public RgbaColor getColor() {
        return color;
    }

    public String getString() {
        return string;
    }

    public TokenType getType() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case COMMAND:
            case FUNCTION:
                return string;
            case VARIABLE:
                return '$' + string;
            case STRING:
                return '"' + string + '"';
            case NUMBER:
                return Double.toString(number);
            case COLOR:
                return color.toString();
            case SEED:
                return ":" + seed;
            case LPAR:
                return "(";
            case RPAR:
                return ")";
            case ASSIGNMENT:
                return "=";
            case COMMA:
                return ",";
            default:
                return type.toString();
        }
    }

    public static Token lpar(CodePosition pos) { return new Token(TokenType.LPAR, pos); }

    public static Token rpar(CodePosition pos) { return new Token(TokenType.RPAR, pos); }

    public static Token assignment(CodePosition pos) { return new Token(TokenType.ASSIGNMENT, pos); }

    public static Token comma(CodePosition pos) { return new Token(TokenType.COMMA, pos); }

    public static Token command(CodePosition pos, String command) {
        return new Token(pos, TokenType.COMMAND, null, command, 0.0, 0);
    }

    public static Token function(CodePosition pos, String function) {
        return new Token(pos, TokenType.FUNCTION, null, function, 0.0, 0);
    }

    public static Token variable(CodePosition pos, String variable) {
        return new Token(pos, TokenType.VARIABLE, null, variable, 0.0, 0);
    }

    public static Token string(CodePosition pos, String string) {
        return new Token(pos, TokenType.STRING, null, string, 0.0, 0);
    }

    public static Token color(CodePosition pos, RgbaColor color) {
        return new Token(pos, TokenType.COLOR, color, null, 0.0, 0);
    }

    public static Token number(CodePosition pos, double number) {
        return new Token(pos, TokenType.NUMBER, null, null, number, 0);
    }

    public static Token seed(CodePosition pos, int seed) {
        return new Token(pos, TokenType.SEED, null, null, 0.0, seed);
    }
}
