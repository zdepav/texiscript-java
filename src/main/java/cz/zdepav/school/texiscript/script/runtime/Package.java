package cz.zdepav.school.texiscript.script.runtime;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

import java.util.HashMap;
import java.util.Map;

/** Package in the standard library. */
public class Package {

    /** Helper functional interface for standard library functions with no arguments. */
    @FunctionalInterface
    public interface BuildFrom0 {

        /** Builds the generator. */
        Generator build(CodePosition line);
    }

    /** Helper functional interface for standard library functions with one argument. */
    @FunctionalInterface
    public interface BuildFrom1 {

        /** Builds the generator. */
        Generator build(CodePosition pos, Generator g1);
    }

    /** Helper functional interface for standard library functions with two arguments. */
    @FunctionalInterface
    public interface BuildFrom2 {

        /** Builds the generator. */
        Generator build(CodePosition pos, Generator g1, Generator g2) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with three arguments. */
    @FunctionalInterface
    public interface BuildFrom3 {

        /** Builds the generator. */
        Generator build(CodePosition pos, Generator g1, Generator g2, Generator g3) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with four arguments. */
    @FunctionalInterface
    public interface BuildFrom4 {

        /** Builds the generator. */
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4
        ) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with five arguments. */
    @FunctionalInterface
    public interface BuildFrom5 {

        /** Builds the generator. */
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4, Generator g5
        ) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with six arguments. */
    @FunctionalInterface
    public interface BuildFrom6 {

        /** Builds the generator. */
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4, Generator g5, Generator g6
        ) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with seven arguments. */
    @FunctionalInterface
    public interface BuildFrom7 {

        /** Builds the generator. */
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3,
            Generator g4, Generator g5, Generator g6, Generator g7
        ) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with eight arguments. */
    @FunctionalInterface
    public interface BuildFrom8 {

        /** Builds the generator. */
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4,
            Generator g5, Generator g6, Generator g7, Generator g8
        ) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with nine arguments. */
    @FunctionalInterface
    public interface BuildFrom9 {

        /** Builds the generator. */
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4,
            Generator g5, Generator g6, Generator g7, Generator g8, Generator g9
        ) throws SemanticException;
    }

    /** Helper functional interface for standard library functions with ten arguments. */
    @FunctionalInterface
    public interface BuildFrom10 {

        /** Builds the generator. */
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4,
            Generator g5, Generator g6, Generator g7, Generator g8, Generator g9, Generator g10
        ) throws SemanticException;
    }

    /** functions in the package mapped to their names */
    private final Map<String, Function> fns;

    /** variables in the package mapped to their names */
    private final Map<String, Generator> vals;

    /** package name */
    public final String name;

    /** Constructs an empty package. */
    public Package(String name) {
        this.name = name;
        fns = new HashMap<>();
        vals = new HashMap<>();
    }

    /**
     * Adds a function with a given name.
     * @param name function name
     * @param builder function implementation
     * @return this
     */
    public Package funcAny(String name, Function builder) {
        fns.put(name, builder);
        return this;
    }

    /**
     * Adds a function with a given name and no arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom0 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 0) {
                throw new SemanticException(pos, this.name + "." + name + " requires 0 arguments");
            } else {
                return builder.build(pos);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and one argument.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom1 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 1) {
                throw new SemanticException(pos, this.name + "." + name + " requires 1 argument");
            } else {
                return builder.build(pos, args[0]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and two arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom2 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 2) {
                throw new SemanticException(pos, this.name + "." + name + " requires 2 arguments");
            } else {
                return builder.build(pos, args[0], args[1]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and three arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom3 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 3) {
                throw new SemanticException(pos, this.name + "." + name + " requires 3 arguments");
            } else {
                return builder.build(pos, args[0], args[1], args[2]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and four arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom4 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 4) {
                throw new SemanticException(pos, this.name + "." + name + " requires 4 arguments");
            } else {
                return builder.build(pos, args[0], args[1], args[2], args[3]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and five arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom5 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 5) {
                throw new SemanticException(pos, this.name + "." + name + " requires 5 arguments");
            } else {
                return builder.build(pos, args[0], args[1], args[2], args[3], args[4]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and six arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom6 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 6) {
                throw new SemanticException(pos, this.name + "." + name + " requires 6 arguments");
            } else {
                return builder.build(pos, args[0], args[1], args[2], args[3], args[4], args[5]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and seven arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom7 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 7) {
                throw new SemanticException(pos, this.name + "." + name + " requires 7 arguments");
            } else {
                return builder.build(pos, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and eight arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom8 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 8) {
                throw new SemanticException(pos, this.name + "." + name + " requires 8 arguments");
            } else {
                return builder.build(pos, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and nine arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom9 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 9) {
                throw new SemanticException(pos, this.name + "." + name + " requires 9 arguments");
            } else {
                return builder.build(
                    pos, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8]
                );
            }
        });
        return this;
    }

    /**
     * Adds a function with a given name and ten arguments.
     * @param name function name
     * @param builder function implementation (generator builder)
     * @return this
     */
    public Package func(String name, BuildFrom10 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 10) {
                throw new SemanticException(pos, this.name + "." + name + " requires 10 arguments");
            } else {
                return builder.build(
                    pos, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]
                );
            }
        });
        return this;
    }

    /**
     * Adds a constant with a given name and value.
     * @param name constant name
     * @param val value
     * @return this
     */
    public Package value(String name, Generator val) {
        vals.put(name, val);
        return this;
    }

    /**
     * Adds a constant with a given name and value.
     * @param name constant name
     * @param val value
     * @return this
     */
    public Package value(String name, int val) {
        vals.put(name, Generator.get(val));
        return this;
    }

    /**
     * Adds a constant with a given name and value.
     * @param name constant name
     * @param val value
     * @return this
     */
    public Package value(String name, double val) {
        vals.put(name, Generator.get(val));
        return this;
    }

    /**
     * Adds a constant with a given name and value.
     * @param name constant name
     * @param val value
     * @return this
     */
    public Package value(String name, RgbaColor val) {
        vals.put(name, Generator.get(val));
        return this;
    }

    /**
     * Gets a function with a given name.
     * @param name name of the requested funcion
     * @return requested function or null if no such function exists
     */
    public Function getFunc(String name) {
        return fns.getOrDefault(name, null);
    }

    /**
     * Gets a value with a given name.
     * @param name name of the requested constant
     * @return requested value or null if no such constant exists
     */
    public Generator getValue(String name) {
        return vals.getOrDefault(name, null);
    }

    /**
     * Iterates over all constants.
     * @return Iterable with name-value pairs
     */
    public Iterable<Map.Entry<String, Generator>> listValues() {
        return vals.entrySet();
    }

    /** Removes all functions and constants from this package. */
    public void clear() {
        fns.clear();
        vals.clear();
    }
}
