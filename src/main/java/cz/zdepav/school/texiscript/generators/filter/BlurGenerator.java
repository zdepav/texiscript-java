package cz.zdepav.school.texiscript.generators.filter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class BlurGenerator extends Generator {

    private final Generator base, hstrength, vstrength;
    private final boolean tiled;
    private double offset;
    private int initializedSize;

    private BlurGenerator(Generator base, Generator hstrength, Generator vstrength, boolean tiled) {
        this.base = base;
        this.hstrength = hstrength;
        this.vstrength = vstrength;
        this.tiled = tiled;
        initializedSize = -1;
        offset = 0;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        var hstr = (int)Math.round(Utils.clamp(hstrength.getDouble(x, y), 0, 4) * 0.25 / offset);
        var vstr = (int)Math.round(Utils.clamp(vstrength.getDouble(x, y), 0, 4) * 0.25 / offset);
        var sum = 0.0;
        var color = RgbaColor.black;
        if (hstr <= 0) {
            if (vstr <= 0) {
                return colorAt(x, y);
            } else {
                var vsigma = vstr / 2.0;
                for (var j = -vstr; j <= vstr; ++j) {
                    var k = gaussianDistribution(j, vsigma);
                    color = color.add(colorAt(x, y + offset * j).multiply(k));
                    sum += k;
                }
            }
        } else {
            var hsigma = hstr / 2.0;
            if (vstr <= 0) {
                for (var i = -hstr; i <= hstr; ++i) {
                    var k = gaussianDistribution(i, hsigma);
                    color = color.add(colorAt(x + offset * i, y).multiply(k));
                    sum += k;
                }
            } else {
                var vsigma = vstr / 2.0;
                for (var i = -hstr; i <= hstr; ++i) {
                    for (var j = -vstr; j <= vstr; ++j) {
                        var k = gaussianDistribution(i, hsigma) * gaussianDistribution(j, vsigma);
                        color = color.add(colorAt(x + offset * i, y + offset * j).multiply(k));
                        sum += k;
                    }
                }
            }
        }
        return color.divide(sum);
    }

    private RgbaColor colorAt(double x, double y) {
        return tiled ? base.getColor(Utils.wrap(x), Utils.wrap(y)) : base.getColor(x, y);
    }

    @Override
    public double getDouble(double x, double y) {
        var hstr = (int)Math.round(Utils.clamp(hstrength.getDouble(x, y), 0, 4) * 0.25 / offset);
        var vstr = (int)Math.round(Utils.clamp(vstrength.getDouble(x, y), 0, 4) * 0.25 / offset);
        var sum = 0.0;
        var number = 0.0;
        if (hstr <= 0) {
            if (vstr <= 0) {
                return doubleAt(x, y);
            } else {
                var vsigma = vstr / 2.0;
                for (var j = -vstr; j <= vstr; ++j) {
                    var k = gaussianDistribution(j, vsigma);
                    number += doubleAt(x, y + offset * j) * k;
                    sum += k;
                }
            }
        } else {
            var hsigma = hstr / 2.0;
            if (vstr <= 0) {
                for (var i = -hstr; i <= hstr; ++i) {
                    var k = gaussianDistribution(i, hsigma);
                    number += doubleAt(x + offset * i, y) * k;
                    sum += k;
                }
            } else {
                var vsigma = vstr / 2.0;
                for (var i = -hstr; i <= hstr; ++i) {
                    for (var j = -vstr; j <= vstr; ++j) {
                        var k = gaussianDistribution(i, hsigma) * gaussianDistribution(j, vsigma);
                        number += doubleAt(x + offset * i, y + offset * j) * k;
                        sum += k;
                    }
                }
            }
        }
        return number / sum;
    }

    private double doubleAt(double x, double y) {
        return tiled ? base.getDouble(Utils.wrap(x), Utils.wrap(y)) : base.getDouble(x, y);
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
        hstrength.init(outputSize, randomize);
        vstrength.init(outputSize, randomize);
        if (outputSize != initializedSize) {
            initializedSize = outputSize;
            offset = 1.0 / outputSize;
        }
    }

    private double gaussianDistribution(double x, double sigma) {
        return Math.exp(-x * x / (2.0 * sigma * sigma)) * (1.0 / (Math.sqrt(2.0 * Math.PI) * sigma));
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 1:
                var strength = Generator.get(0.2);
                return new BlurGenerator(args[0], strength, strength, false);
            case 2:
                return new BlurGenerator(args[0], args[1], args[1], false);
            case 3:
                return new BlurGenerator(args[0], args[1], args[2], false);
            case 4:
                if (!args[3].isNumber()) {
                    throw new SemanticException(pos, "filter.blur expects a boolean as its fourth argument");
                }
                return new BlurGenerator(args[0], args[1], args[2], (int)args[3].getDouble(0, 0) != 0);
            default:
                throw new SemanticException(pos, "filter.blur requires 1 to 4 arguments");
        }
    }
}
