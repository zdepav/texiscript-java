package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Vec2;

/** @author Zdenek Pavlatka */
public class TranslationGenerator extends TransformationGenerator {

    private final Generator x, y;

    public TranslationGenerator(CodePosition pos, Generator base, Generator x, Generator y) {
        super(base);
        this.x = x;
        this.y = y;
    }

    @Override
    protected Vec2 reverseTransform(double x, double y) {
        return new Vec2(x + 0.5 - this.x.getDouble(x, y), y + 0.5 - this.y.getDouble(x, y));
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        super.init(outputSize, randomize);
        x.init(outputSize, randomize);
        y.init(outputSize, randomize);
    }
}
