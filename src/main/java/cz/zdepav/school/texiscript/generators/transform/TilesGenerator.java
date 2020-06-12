package cz.zdepav.school.texiscript.generators.transform;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** Repeats its input. */
public class TilesGenerator extends Generator {

    private final Generator base;
    private final double horizontalCount, verticatCount;

    public TilesGenerator(Generator base, int horizontalCount, int verticatCount) {
        this.base = base;
        this.horizontalCount = horizontalCount;
        this.verticatCount = verticatCount;
    }

    /** {@inheritDoc} */
    @Override
    public RgbaColor getColor(double x, double y) {
        return base.getColor(x * horizontalCount % 1, y * verticatCount % 1);
    }

    /** {@inheritDoc} */
    @Override
    public double getDouble(double x, double y) {
        return base.getDouble(x * horizontalCount % 1, y * verticatCount % 1);
    }

    /** {@inheritDoc} */
    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 2:
                if (args[1].isNumber()) {
                    int count = Utils.clamp((int)args[1].getDouble(0, 0), 1, 1000);
                    return new TilesGenerator(args[0], count, count);
                } else {
                    throw new SemanticException(pos, "transform.tiles expects a number as its second argument");
                }
            case 3:
                if (args[1].isNumber()) {
                    if (args[2].isNumber()) {
                        return new TilesGenerator(
                            args[0],
                            Utils.clamp((int)args[1].getDouble(0, 0), 1, 1000),
                            Utils.clamp((int)args[2].getDouble(0, 0), 1, 1000)
                        );
                    } else {
                        throw new SemanticException(pos, "transform.tiles expects a number as its third argument");
                    }
                } else {
                    throw new SemanticException(pos, "transform.tiles expects a number as its second argument");
                }
            default:
                throw new SemanticException(pos, "transform.tiles requires 2 or 3 arguments");
        }
    }
}
