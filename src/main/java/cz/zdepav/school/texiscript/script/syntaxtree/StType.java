package cz.zdepav.school.texiscript.script.syntaxtree;

/** Represents the type an expression results in. */
public enum StType {
    STRING,
    COLOR,
    NUMBER,
    GENERATOR,
    VOID;

    /**
     * Checks if given type is a subtype of this.
     * @param type type to check
     * @return true if given type is a subtype of this, false otherwise
     */
    public boolean isAssignableFrom(StType type) {
        switch (this) {
            case STRING:
                return type == STRING;
            case COLOR:
                return type == COLOR;
            case NUMBER:
                return type == NUMBER;
            case GENERATOR:
                return type != VOID && type != STRING;
            default:
                return false;
        }
    }

    /**
     * Checks if the type associated with the given class is a subtype of this.
     * @param className class name of syntax tree node type
     * @return true if the type associated with the given class is a subtype of this, false otherwise
     */
    public boolean isAssignableFrom(String className) {
        return isAssignableFrom(get(className));
    }

    /**
     * Checks if given syntax tree node's type is a subtype of this.
     * @param arg syntax tree node to check
     * @return true if given syntax tree node's type is a subtype of this, false otherwise
     */
    public boolean isAssignableFrom(StCommandArgument arg) {
        return isAssignableFrom(get(arg.getClass().getSimpleName()));
    }

    /**
     * Checks if the type associated with the given class is a subtype of this.
     * @param cls class of syntax tree node type
     * @return true if the type associated with the given class is a subtype of this, false otherwise
     */
    public boolean isAssignableFrom(Class<? extends StCommandArgument> cls) {
        return isAssignableFrom(get(cls.getSimpleName()));
    }

    @Override
    public String toString() {
        switch (this) {
            case STRING:
                return "string";
            case COLOR:
                return "color";
            case NUMBER:
                return "number";
            case GENERATOR:
                return "generator";
            default:
                return "void";
        }
    }

    /**
     * Gets the type associated with the given class.
     * @param className class name of syntax tree node type
     * @return type associated with the given class
     */
    public static StType get(String className) {
        switch (className) {
            case "StString":
                return STRING;
            case "StColor":
                return COLOR;
            case "StNumber":
                return NUMBER;
            case "StGenerator":
            case "StFunctionCall":
            case "StVariable":
                return GENERATOR;
            default:
                return VOID;
        }
    }

    /**
     * Gets the type associated with the given syntax tree node.
     * @param arg syntax tree node to get type of
     * @return type associated with the given syntax tree node
     */
    public static StType get(StCommandArgument arg) {
        return get(arg.getClass().getSimpleName());
    }

    /**
     * Gets the type associated with the given class.
     * @param cls class of syntax tree node type
     * @return type associated with the given class
     */
    public static StType get(Class<? extends StCommandArgument> cls) {
        return get(cls.getSimpleName());
    }
}
