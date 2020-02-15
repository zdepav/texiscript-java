package cz.zdepav.school.texiscript.script.syntaxtree;

/** @author Zdenek Pavlatka */
public enum StType {
    STRING,
    COLOR,
    NUMBER,
    GENERATOR,
    VOID;

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

    public boolean isAssignableFrom(String className) {
        return isAssignableFrom(get(className));
    }

    public boolean isAssignableFrom(StCommandArgument arg) {
        return isAssignableFrom(get(arg.getClass().getSimpleName()));
    }

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

    public static StType get(StCommandArgument arg) {
        return get(arg.getClass().getSimpleName());
    }

    public static StType get(Class<? extends StCommandArgument> cls) {
        return get(cls.getSimpleName());
    }
}
