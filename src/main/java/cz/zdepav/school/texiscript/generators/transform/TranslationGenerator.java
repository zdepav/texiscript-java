package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Vec2;

/** Offsets its input. */
public class TranslationGenerator extends TransformationGenerator {

    private final double x, y;

    public TranslationGenerator(CodePosition pos, Generator base, Generator x, Generator y) throws SemanticException {
        super(base);
        if (!x.isNumber()) {
            throw new SemanticException(pos, "transform.translate expects a number as its first argument");
        }
        if (!y.isNumber()) {
            throw new SemanticException(pos, "transform.translate expects a number as its second argument");
        }
        this.x = x.getDouble(0, 0);
        this.y = y.getDouble(0, 0);
    }

    /** {@inheritDoc} */
    @Override
    protected Vec2 reverseTransform(double x, double y) {
        return new Vec2(x - this.x, y - this.y);
    }
}
