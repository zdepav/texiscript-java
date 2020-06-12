package cz.zdepav.school.texiscript.script.interpreter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.syntaxtree.StType;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** Represents a parsed command argument. */
public class TypedArgument {

    /** argument type */
    private final StType type;

    /** string value or null */
    private final String string;

    /** generator value or null */
    private final Generator generator;

    /**
     * Constructs a TypedArgument from a string.
     * @param string argument value
     */
    public TypedArgument(String string) {
        this.string = string;
        generator = null;
        type = StType.STRING;
    }

    /**
     * Constructs a TypedArgument from a generator.
     * @param generator argument value
     */
    public TypedArgument(Generator generator) {
        this.generator = generator;
        string = null;
        type = generator.isNumber() ? StType.NUMBER : generator.isColor() ? StType.COLOR : StType.GENERATOR;
    }

    /** Gets argument type. */
    public StType getType() {
        return type;
    }

    /**
     * Gets string value.
     * @return argument value
     * @throws RuntimeException When type is not string.
     */
    public String getString() {
        if (type != StType.STRING) {
            throw new RuntimeException("getString can only be used on string typed argument");
        }
        return string;
    }

    /**
     * Gets generator value.
     * @return argument value
     * @throws RuntimeException When type is not generator.
     */
    public Generator getGenerator() {
        if (type == StType.STRING) {
            throw new RuntimeException("getGenerator can only be used on generator typed argument");
        }
        return generator;
    }

    /**
     * Gets numeric value.
     * @return argument value
     * @throws RuntimeException When type is not number.
     */
    public double getNumber() {
        if (type != StType.NUMBER) {
            throw new RuntimeException("getNumber can only be used on numeric argument");
        }
        return generator.getDouble(0, 0);
    }

    /**
     * Gets color value.
     * @return argument value
     * @throws RuntimeException When type is not color.
     */
    public RgbaColor getColor() {
        if (type != StType.COLOR) {
            throw new RuntimeException("getColor can only be used on color typed argument");
        }
        return generator.getColor(0, 0);
    }
}
