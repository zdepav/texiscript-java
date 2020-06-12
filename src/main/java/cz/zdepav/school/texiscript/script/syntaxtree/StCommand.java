package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** A node in the syntactic tree that represents a command. */
public class StCommand extends StNode {

    /** command name */
    private final String name;

    /** command arguments */
    private final StCommandArgument[] arguments;

    public StCommand(CodePosition pos, String name, StCommandArgument[] arguments) {
        super(pos);
        this.name = name;
        this.arguments = arguments;
    }

    /** Gets command name. */
    public String getName() {
        return name;
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
    public StCommandArgument getArgument(int i) {
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
        sb.append(")\n");
        return sb.toString();
    }
}
