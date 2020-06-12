package cz.zdepav.school.texiscript.script.parser;

import cz.zdepav.school.texiscript.script.syntaxtree.*;

import java.util.ArrayList;
import java.util.List;

/** A basic code parser, builds a syntax tree from a sequence of tokens. */
public class Parser {

    /** input token sequence */
    private final TokenSource tokenSource;

    /** Constructs a parser from a token source. */
    public Parser(TokenSource tokenSource) {
        this.tokenSource = tokenSource;
    }

    /**
     * Helper method for exception construction.
     * @param badToken token that caused the error
     * @param message error message
     * @return the constructed exception
     */
    private SyntaxException error(Token badToken, String message) {
        return new SyntaxException(badToken.getCodePosition(), message);
    }

    /**
     * Helper method for exception construction.
     * @param message error message
     * @return the constructed exception
     */
    private SyntaxException error(String message) {
        return new SyntaxException(tokenSource.getCodePosition(), message);
    }

    /**
     * Reads next token and checks whether it is of the correct type before returning it.
     * @param expectedType expected token type
     * @return the read token
     * @throws SyntaxException When the next token is of a wrong type or EOF was reached.
     */
    private Token checkToken(TokenType expectedType) throws SyntaxException {
        var t = tokenSource.readToken();
        if (t == null) {
            throw error("Unexpected end of input, expected '" + expectedType + "'");
        }
        if (t.getType() != expectedType) {
            throw error(t, "Unexpected token '" + t + "', expected '" + expectedType + "'");
        }
        return t;
    }

    /**
     * Returns type of the next token without advancing position.
     * @return type of the next token
     * @throws SyntaxException When EOF was reached.
     */
    private TokenType peekTokenType() throws SyntaxException {
        var t = tokenSource.peekToken();
        if (t == null) {
            throw error("Unexpected end of input");
        }
        return t.getType();
    }

    /**
     * Syntax: ( command | assignment )+
     * @return list of parsed tree nodes
     * @throws SyntaxException When the script contains errors
     */
    public List<StNode> parseScript() throws SyntaxException {
        var nodes = new ArrayList<StNode>();
        Token t;
        while ((t = tokenSource.peekToken()) != null) {
            switch (t.getType()) {
                case COMMAND:
                    nodes.add(parseCommand());
                    continue;
                case VARIABLE:
                    nodes.add(parseAssignment());
                    continue;
                default:
                    throw error(t, "Unexpected token '" + t + "', expected command or variable");
            }
        }
        return nodes;
    }

    /**
     * Syntax: COMMAND LPAR command_argument ( COMMA command_argument )* RPAR
     * @return parsed command node
     * @throws SyntaxException When the script contains errors
     */
    private StCommand parseCommand() throws SyntaxException {
        var cmd = checkToken(TokenType.COMMAND);
        var posA = cmd.getCodePosition();
        checkToken(TokenType.LPAR);
        var args = new ArrayList<StCommandArgument>();
        args.add(parseCommandArgument());
        Token t;
        loop:
        while ((t = tokenSource.peekToken()) != null) {
            switch (t.getType()) {
                case RPAR:
                    break loop;
                case COMMA:
                    tokenSource.skipToken();
                    args.add(parseCommandArgument());
                    continue;
                default:
                    throw error(t, "Unexpected token '" + t + "', expected ')' or ','");
            }
        }
        var rpar = checkToken(TokenType.RPAR);
        return new StCommand(posA.until(rpar.getCodePosition()), cmd.getString(), args.toArray(new StCommandArgument[args.size()]));
    }

    /**
     * Syntax: STRING | generator
     * @return parsed command argument node
     * @throws SyntaxException When the script contains errors
     */
    private StCommandArgument parseCommandArgument() throws SyntaxException {
        if (peekTokenType() == TokenType.STRING) {
            var token = tokenSource.readToken();
            return new StString(token.getCodePosition(), token.getString());
        } else return parseGenerator();
    }

    /**
     * Syntax: NUMBER | COLOR | VARIABLE | function_call
     * @return parsed generator node
     * @throws SyntaxException When the script contains errors
     */
    private StGenerator parseGenerator() throws SyntaxException {
        Token token;
        switch (peekTokenType()) {
            case NUMBER:
                token = tokenSource.readToken();
                return new StNumber(token.getCodePosition(), token.getNumber());
            case COLOR:
                token = tokenSource.readToken();
                return new StColor(token.getCodePosition(), token.getColor());
            case VARIABLE:
                token = tokenSource.readToken();
                return new StVariable(token.getCodePosition(), token.getString());
            case FUNCTION:
                return parseFunctionCall();
            default:
                token = tokenSource.readToken();
                throw error(
                    token,
                    "Unexpected token '" + token + "', expected number, color, variable or function call"
                );
        }
    }

    /**
     * Syntax: FUNCTION SEED? LPAR ( generator ( COMMA generator )* )? RPAR
     * @return parsed function call node
     * @throws SyntaxException When the script contains errors
     */
    private StFunctionCall parseFunctionCall() throws SyntaxException {
        var fn = checkToken(TokenType.FUNCTION);
        var posA = fn.getCodePosition();
        var seed = peekTokenType() == TokenType.SEED ? tokenSource.readToken() : null;
        checkToken(TokenType.LPAR);
        var args = new ArrayList<StGenerator>();
        if (peekTokenType() != TokenType.RPAR) {
            args.add(parseGenerator());
            Token t;
            loop:
            while ((t = tokenSource.peekToken()) != null) {
                switch (t.getType()) {
                    case RPAR:
                        break loop;
                    case COMMA:
                        tokenSource.skipToken();
                        args.add(parseGenerator());
                        continue;
                    default:
                        throw error(t, "Unexpected token '" + t + "', expected ')' or ','");
                }
            }
        }
        var rpar = checkToken(TokenType.RPAR);
        return new StFunctionCall(
            posA.until(rpar.getCodePosition()),
            fn.getString(),
            args.toArray(new StGenerator[args.size()]),
            seed != null ? seed.getSeed() : 0
        );
    }

    /**
     * Syntax: VARIABLE ASSIGNMENT generator
     * @return parsed assignment node
     * @throws SyntaxException When the script contains errors
     */
    private StAssignment parseAssignment() throws SyntaxException {
        var var = checkToken(TokenType.VARIABLE);
        var posA = var.getCodePosition();
        if (var.getString().indexOf('.') >= 0) {
            throw error(var, "Custom variables can't be placed into packages.");
        }
        checkToken(TokenType.ASSIGNMENT);
        var gen = parseGenerator();
        return new StAssignment(posA.until(gen.getCodePosition()), var.getString(), gen);
    }
}
