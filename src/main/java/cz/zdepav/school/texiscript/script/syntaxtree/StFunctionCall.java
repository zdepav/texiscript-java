package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that represents a function call. */
public class StFunctionCall extends StGenerator {

    /** function name */
    private final String name;

    /** function arguments */
    private final StGenerator[] arguments;

    /** seed for random generator */
    private final int seed;

    public StFunctionCall(CodePosition pos, String name, StGenerator[] arguments, int seed) {
        super(pos);
        this.name = name;
        this.arguments = arguments;
        this.seed = seed;
    }

    /** Gets function name. */
    public String getName() {
        return name;
    }

    /** Gets seed for random generator. */
    public int getSeed() {
        return seed;
    }

    /** Gets command argument count. */
    public int getArgumentCount() {
        return arguments.length;
    }

    /**
     * Gets command argument.
     * @param i argument index
     * @return argument at the given index
     */
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
