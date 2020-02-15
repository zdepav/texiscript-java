package cz.zdepav.school.texiscript.script.parser;

/** @author Zdenek Pavlatka */
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
    COMMA       // ,
}

