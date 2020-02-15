package cz.zdepav.school.texiscript.script.runtime;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;

import java.util.HashMap;
import java.util.Map;

/** @author Zdenek Pavlatka */
public class Package {

    public interface BuildFrom0 {
        Generator build(CodePosition line);
    }

    public interface BuildFrom1 {
        Generator build(CodePosition pos, Generator g1);
    }

    public interface BuildFrom2 {
        Generator build(CodePosition pos, Generator g1, Generator g2) throws SemanticException;
    }

    public interface BuildFrom3 {
        Generator build(CodePosition pos, Generator g1, Generator g2, Generator g3) throws SemanticException;
    }

    public interface BuildFrom4 {
        Generator build(CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4) throws SemanticException;
    }

    public interface BuildFrom5 {
        Generator build(CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4, Generator g5) throws SemanticException;
    }

    public interface BuildFrom6 {
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4, Generator g5, Generator g6
        ) throws SemanticException;
    }

    public interface BuildFrom7 {
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3,
            Generator g4, Generator g5, Generator g6, Generator g7
        ) throws SemanticException;
    }

    public interface BuildFrom8 {
        Generator build(
            CodePosition pos, Generator g1, Generator g2, Generator g3, Generator g4,
            Generator g5, Generator g6, Generator g7, Generator g8
        ) throws SemanticException;
    }

    private Map<String, Function> fns;
    private Map<String, Generator> vals;
    public final String name;

    public Package(String name) {
        this.name = name;
        fns = new HashMap<>();
        vals = new HashMap<>();
    }

    public Package func(String name, Function builder) {
        fns.put(name, builder);
        return this;
    }

    public Package func0(String name, BuildFrom0 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 0) {
                throw new SemanticException(pos, name + " requires 0 arguments");
            } else return builder.build(pos);
        });
        return this;
    }

    public Package func1(String name, BuildFrom1 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 1) {
                throw new SemanticException(pos, name + " requires 1 argument");
            } else return builder.build(pos, args[0]);
        });
        return this;
    }

    public Package func2(String name, BuildFrom2 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 2) {
                throw new SemanticException(pos, name + " requires 2 arguments");
            } else return builder.build(pos, args[0], args[1]);
        });
        return this;
    }

    public Package func3(String name, BuildFrom3 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 3) {
                throw new SemanticException(pos, name + " requires 3 arguments");
            } else return builder.build(pos, args[0], args[1], args[2]);
        });
        return this;
    }

    public Package func4(String name, BuildFrom4 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 4) {
                throw new SemanticException(pos, name + " requires 4 arguments");
            } else return builder.build(pos, args[0], args[1], args[2], args[3]);
        });
        return this;
    }

    public Package func5(String name, BuildFrom5 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 5) {
                throw new SemanticException(pos, name + " requires 5 arguments");
            } else return builder.build(pos, args[0], args[1], args[2], args[3], args[4]);
        });
        return this;
    }

    public Package func6(String name, BuildFrom6 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 6) {
                throw new SemanticException(pos, name + " requires 6 arguments");
            } else return builder.build(pos, args[0], args[1], args[2], args[3], args[4], args[5]);
        });
        return this;
    }

    public Package func7(String name, BuildFrom7 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 7) {
                throw new SemanticException(pos, name + " requires 7 arguments");
            } else return builder.build(pos, args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
        });
        return this;
    }

    public Package func8(String name, BuildFrom8 builder) {
        fns.put(name, (pos, args) -> {
            if (args.length != 8) {
                throw new SemanticException(pos, name + " requires 8 arguments");
            } else return builder.build(pos, args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
        });
        return this;
    }

    public Package value(String name, Generator val) {
        vals.put(name, val);
        return this;
    }

    public Package value(String name, int val) {
        vals.put(name, Generator.get(val));
        return this;
    }

    public Package value(String name, double val) {
        vals.put(name, Generator.get(val));
        return this;
    }

    public Package value(String name, RgbaColor val) {
        vals.put(name, Generator.get(val));
        return this;
    }

    public Function getFunc(String name) {
        return fns.getOrDefault(name, null);
    }

    public Generator getValue(String name) {
        return vals.getOrDefault(name, null);
    }

    public Iterable<Map.Entry<String, Generator>> listValues() {
        return vals.entrySet();
    }

    public void clear() {
        fns.clear();
        vals.clear();
    }
}
