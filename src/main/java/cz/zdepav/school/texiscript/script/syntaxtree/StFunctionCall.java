package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class StFunctionCall extends StGenerator {

    private final String name;
    private final StGenerator[] arguments;
    private final int seed;

    public StFunctionCall(CodePosition pos, String name, StGenerator[] arguments, int seed) {
        super(pos);
        this.name = name;
        this.arguments = arguments;
        this.seed = seed;
    }

    public String getName() {
        return name;
    }

    public int getSeed() {
        return seed;
    }

    public int getArgumentCount() {
        return arguments.length;
    }

    public StGenerator getArgument(int i) {
        return arguments[i];
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(name);
        sb.append('(');
        for (int i = 0; i < arguments.length; ++i) {
            sb.append(arguments[i]);
            if (i < arguments.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
