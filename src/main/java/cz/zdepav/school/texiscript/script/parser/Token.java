package cz.zdepav.school.texiscript.script.parser;

import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Represents  a lexer-generated token. */
public class Token {

    /** position in the source code */
    private final CodePosition pos;

    /** seed / variant value (if relevant) */
    private final int seed;

    /** numeric value (if relevant) */
    private final double number;

    /** color value (if relevant) */
    private final RgbaColor color;

    /** string value (if relevant) */
    private final String string;

    /** token type */
    private final TokenType type;

    /** Constructs a token. */
    private Token(CodePosition pos, TokenType type, RgbaColor color, String string, double number, int seed) {
        this.pos = pos;
        this.type = type;
        this.seed = seed;
        this.color = color;
        this.string = string;
        this.number = number;
    }

    /** Constructs a token without a value. */
    private Token(TokenType type, CodePosition pos) {
        this(pos, type, null, null, 0, 0);
    }

    /** Gets position in the source code. */
    public CodePosition getCodePosition() {
        return pos;
    }

    /** Gets numeric value. */
    double getNumber() {
        return number;
    }

    /** Gets seed value. */
    public int getSeed() {
        return seed;
    }

    /** Gets color value. */
    public RgbaColor getColor() {
        return color;
    }

    /** Gets string value. */
    public String getString() {
        return string;
    }

    /** Gets token type. */
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

    /** Constructs a LPAR token. ("(") */
    public static Token lpar(CodePosition pos) { return new Token(TokenType.LPAR, pos); }

    /** Constructs a RPAR token. (")") */
    public static Token rpar(CodePosition pos) { return new Token(TokenType.RPAR, pos); }

    /** Constructs a ASSIGNMENT token. ("=") */
    public static Token assignment(CodePosition pos) { return new Token(TokenType.ASSIGNMENT, pos); }

    /** Constructs a COMMA token. (",") */
    public static Token comma(CodePosition pos) { return new Token(TokenType.COMMA, pos); }

    /** Constructs a COMMAND token. ("[A-Z][a-z]*") */
    public static Token command(CodePosition pos, String command) {
        return new Token(pos, TokenType.COMMAND, null, command, 0.0, 0);
    }

    /** Constructs a FUNCTION token. ("([a-z]+\.)*[a-z]+") */
    public static Token function(CodePosition pos, String function) {
        return new Token(pos, TokenType.FUNCTION, null, function, 0.0, 0);
    }

    /** Constructs a VARIABLE token. ("$([a-z]+\.)*[A-Za-z0-9_]+") */
    public static Token variable(CodePosition pos, String variable) {
        return new Token(pos, TokenType.VARIABLE, null, variable, 0.0, 0);
    }

    /** Constructs a STRING token. ("\"([^\"]+|\\\")*\"") */
    public static Token string(CodePosition pos, String string) {
        return new Token(pos, TokenType.STRING, null, string, 0.0, 0);
    }

    /** Constructs a COLOR token. ("#([0-9a-fA-F]{3,4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})") */
    public static Token color(CodePosition pos, RgbaColor color) {
        return new Token(pos, TokenType.COLOR, color, null, 0.0, 0);
    }

    /** Constructs a NUMBER token. ("[+-]?[0-9]+(\.[0-9]+)?([eE][+-]?[0-9]+)?") */
    public static Token number(CodePosition pos, double number) {
        return new Token(pos, TokenType.NUMBER, null, null, number, 0);
    }

    /** Constructs a SEED token. (":[0-9]+") */
    public static Token seed(CodePosition pos, int seed) {
        return new Token(pos, TokenType.SEED, null, null, 0.0, seed);
    }
}
