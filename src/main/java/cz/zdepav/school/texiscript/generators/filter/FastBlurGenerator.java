package cz.zdepav.school.texiscript.generators.filter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

/** @author Zdenek Pavlatka */
public class FastBlurGenerator extends Generator {

    private final Generator base;
    private final double strength;
    private int initializedSize;
    private RgbaColor[] image;

    private FastBlurGenerator(Generator base, double strength) {
        this.base = base;
        this.strength = strength;
        image = null;
        initializedSize = -1;
    }

    @Override
    public RgbaColor getColor(double x, double y) {
        // uses linear interpolation, texture is tiled
        var _x = x * initializedSize;
        var _y = y * initializedSize;
        var x0 = Utils.wrap((int)_x, initializedSize);
        var y0 = Utils.wrap((int)_y, initializedSize);
        var x1 = (x0 + 1) % initializedSize;
        var y1 = (y0 + 1) % initializedSize * initializedSize;
        y0 *= initializedSize;
        var xd = Utils.wrap(_x, initializedSize) % 1;
        var yd = Utils.wrap(_y, initializedSize) % 1;
        var c00 = image[x0 + y0];
        var c01 = image[x0 + y1];
        var c10 = image[x1 + y0];
        var c11 = image[x1 + y1];
        return c00.lerp(c10, xd).lerp(c01.lerp(c11, xd), yd);
    }

    @Override
    public double getDouble(double x, double y) {
        return getColor(x, y).intensity;
    }

    @Override
    public void init(int outputSize, boolean randomize) {
        base.init(outputSize, randomize);
        if (outputSize != initializedSize) {
            image = new RgbaColor[outputSize * outputSize];
            var offset = 1.0 / outputSize;
            var str = (int)Math.round(strength * 0.25 / offset);
            var kernel = generateGaussianKernel(str);
            var buffer = new RgbaColor[kernel.length];
            preRenderHorizontal(outputSize, kernel, offset, buffer, str);
            preRenderVertical(outputSize, kernel, buffer, str);
            initializedSize = outputSize;
        }
    }

    private void preRenderHorizontal(int outputSize, double[] kernel, double offset, RgbaColor[] buffer, int str) {
        RgbaColor c;
        for (var y = 0; y < outputSize; ++y) {
            for (var i = 0; i < kernel.length - 1; ++i) {
                buffer[i] = base.getColor((i - str + outputSize) % outputSize * offset, y * offset);
            }
            var _y = y * outputSize;
            for (var x = 0; x < outputSize; ++x) {
                buffer[(kernel.length - 1 + x) % kernel.length] =
                    base.getColor((x + str + outputSize) % outputSize * offset, y * offset);
                c = buffer[x % kernel.length].multiply(kernel[0]);
                for (var i = 1; i < kernel.length; ++i) {
                    c = c.add(buffer[(x + i) % kernel.length].multiply(kernel[i]));
                }
                image[x + _y] = c;
            }
        }
    }

    private void preRenderVertical(int outputSize, double[] kernel, RgbaColor[] buffer, int str) {
        RgbaColor c;
        for (var x = 0; x < outputSize; ++x) {
            for (var i = 0; i < kernel.length - 1; ++i) {
                buffer[i] = image[x + (i - str + outputSize) % outputSize * outputSize];
            }
            for (var y = 0; y < outputSize; ++y) {
                buffer[(kernel.length - 1 + y) % kernel.length] =
                    image[x + (y + str + outputSize) % outputSize * outputSize];
                c = buffer[y % kernel.length].multiply(kernel[0]);
                for (var i = 1; i < kernel.length; ++i) {
                    c = c.add(buffer[(y + i) % kernel.length].multiply(kernel[i]));
                }
                image[x + y * outputSize] = c;
            }
        }
    }

    private double gaussianDistribution(double x, double sigma) {
        return Math.exp(-x * x / (2.0 * sigma * sigma)) * (1.0 / (Math.sqrt(2.0 * Math.PI) * sigma));
    }

    private double[] generateGaussianKernel(int str) {
        var kernelSize = 1 + 2 * str;
        var gaussianKernel = new double[kernelSize];
        if (kernelSize == 1) {
            gaussianKernel[0] = 1;
            return gaussianKernel;
        }
        var sigma = str / 2.0;
        var sum = 0.0;
        for (int i = 0; i < kernelSize; ++i) {
            gaussianKernel[i] = gaussianDistribution(i - str, sigma);
            sum += gaussianKernel[i];
        }
        for (int i = 0; i < kernelSize; ++i) {
            gaussianKernel[i] /= sum;
        }
        return gaussianKernel;
    }

    public static Generator build(CodePosition pos, Generator[] args) throws SemanticException {
        switch (args.length) {
            case 1:
                return new FastBlurGenerator(args[0], 0.2);
            case 2:
                if (!args[1].isNumber()) {
                    throw new SemanticException(pos, "filter.fastblur expects a number as its second argument");
                }
                return new FastBlurGenerator(
                    args[0],
                    Utils.clamp(args[1].getDouble(0, 0), 0, 4)
                );
            default:
                throw new SemanticException(pos, "filter.fastblur requires 1 or 2 arguments");
        }
    }
}
