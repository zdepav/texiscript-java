package cz.zdepav.school.texiscript.script.parser;

import cz.zdepav.school.texiscript.script.syntaxtree.*;

import java.util.ArrayList;
import java.util.List;

/** @author Zdenek Pavlatka */
public class Parser {

    private TokenSource tokenSource;

    public Parser(TokenSource tokenSource) {
        this.tokenSource = tokenSource;
    }

    private SyntaxException error(Token badToken, String message) {
        return new SyntaxException(badToken.getCodePosition(), message);
    }

    private SyntaxException error(String message) {
        return new SyntaxException(tokenSource.getCodePosition(), message);
    }

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

    private TokenType peekTokenType() throws SyntaxException {
        var t = tokenSource.peekToken();
        if (t == null) {
            throw error("Unexpected end of input");
        }
        return t.getType();
    }

    // program: ( command | assignment )+ ;
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

    // command: COMMAND LPAR command_argument ( COMMA command_argument )* RPAR ;
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

    // command_argument: STRING | generator ;
    private StCommandArgument parseCommandArgument() throws SyntaxException {
        if (peekTokenType() == TokenType.STRING) {
            var token = tokenSource.readToken();
            return new StString(token.getCodePosition(), token.getString());
        } else return parseGenerator();
    }

    // generator: NUMBER | COLOR | VARIABLE | function_call ;
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

    // function_call: FUNCTION SEED? LPAR ( generator ( COMMA generator )* )? RPAR ;
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

    // assignment: VARIABLE ASSIGNMENT generator ;
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
