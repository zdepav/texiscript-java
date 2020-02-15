package cz.zdepav.school.texiscript.script.syntaxtree;

import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
public class StCommand extends StNode {

    private final String name;
    private final StCommandArgument[] arguments;

    public StCommand(CodePosition pos, String name, StCommandArgument[] arguments) {
        super(pos);
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public int getArgumentCount() {
        return arguments.length;
    }

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
