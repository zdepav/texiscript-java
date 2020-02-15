package cz.zdepav.school.texiscript.script.interpreter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.syntaxtree.StType;
import cz.zdepav.school.texiscript.utils.RgbaColor;

/** @author Zdenek Pavlatka */
public class TypedArgument {

    private StType type;
    private String string;
    private Generator generator;

    public TypedArgument(String string) {
        this.string = string;
        generator = null;
        type = StType.STRING;
    }

    public TypedArgument(Generator generator) {
        this.generator = generator;
        string = null;
        type = generator.isNumber() ? StType.NUMBER : generator.isColor() ? StType.COLOR : StType.GENERATOR;
    }

    public StType getType() {
        return type;
    }

    public String getString() {
        return string;
    }

    public Generator getGenerator() {
        return generator;
    }

    public double getNumber() {
        return type == StType.NUMBER ? generator.getDouble(0, 0) : 0;
    }

    public RgbaColor getColor() {
        return type == StType.COLOR ? generator.getColor(0, 0) : RgbaColor.black;
    }
}
