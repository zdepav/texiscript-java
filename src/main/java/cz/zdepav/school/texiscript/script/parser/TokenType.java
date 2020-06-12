package cz.zdepav.school.texiscript.script.parser;

enum TokenType {
    COMMAND,    // [A-Z][a-z]*
    FUNCTION,   // ([a-z]+\.)*[a-z]+
    VARIABLE,   // $([a-z]+\.)*[A-Za-z0-9_]+
    STRING,     // "([^"]+|\\")*"
    NUMBER,     // [+-]?[0-9]+(\.[0-9]+)?([eE][+-]?[0-9]+)?
    COLOR,      // #([0-9a-fA-F]{3,4}|[0-9a-fA-F]{6}|[0-9a-fA-F]{8})
    SEED,       // :[0-9]+
    LPAR,       // (
    RPAR,       // )
    ASSIGNMENT, // =
    COMMA;      // ,

    @Override
    public String toString() {
        switch (this) {
            case COMMAND:
                return "command";
            case FUNCTION:
                return "function";
            case VARIABLE:
                return "variable";
            case STRING:
                return "string";
            case NUMBER:
                return "number";
            case COLOR:
                return "color";
            case SEED:
                return "seed";
            case LPAR:
                return "(";
            case RPAR:
                return ")";
            case ASSIGNMENT:
                return "=";
            case COMMA:
                return ",";
            default:
                return "undefined";
        }
    }
}

