package cz.zdepav.school.texiscript.script.interpreter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/** Class that renders images using generators. */
public class ImageRenderer {

    /** used to insert frame index into file name */
    private static final Pattern frameIdLocationPattern = Pattern.compile("#+");

    /** whether generation should be fully randomized or deterministic */
    private boolean randomize;

    /** whether generation time should be measured */
    private boolean measureTime;

    /** width and height of the generated image in pixels */
    private int textureSize;

    /** antialiasing level, when 0, antialiasing is disabled */
    private int textureSmoothing;

    /** name for the generated file */
    private String outputFile;

    /** already generated files */
    private final Set<String> usedOutputFiles;

    /** working directory */
    private final Path directory;

    /**
     * Constructs an image renderer with fixed working directory and default output file name.
     * @param directory working directory
     * @param outputFile default output file name
     */
    public ImageRenderer(Path directory, String outputFile) {
        this.directory = directory;
        randomize = false;
        measureTime = false;
        textureSize = 256;
        textureSmoothing = 1;
        this.outputFile = outputFile;
        usedOutputFiles = new HashSet<>();
    }

    /**
     * Sets output image size in pixels.
     * @param textureSize image size in pixels, must be greater than zero
     * @throws IllegalArgumentException When textureSize \< 1.
     */
    public void setTextureSize(int textureSize) {
        if (textureSize < 1) {
            throw new IllegalArgumentException("textureSize must be positive");
        }
        this.textureSize = textureSize;
    }

    /** Gets output image size in pixels. */
    public int getTextureSize() {
        return textureSize;
    }

    /**
     * Sets whether generation should be fully randomized or deterministic.
     * @param randomize true to enable randomization, false to disable
     */
    public void setRandomization(boolean randomize) {
        this.randomize = randomize;
    }

    /** Gets whether generation should be fully randomized or deterministic. */
    public boolean getRandomization() {
        return randomize;
    }

    /**
     * Sets antialiasing level.
     * @param textureSmoothing antialiasing level between 0 and 10
     * @throws IllegalArgumentException When textureSmoothing \< 0 or textureSmoothing \> 10.
     */
    public void setTextureSmoothing(int textureSmoothing) {
        if (textureSmoothing < 0 || textureSmoothing > 10) {
            throw new IllegalArgumentException("textureSmoothing must be between 0 and 10");
        }
        this.textureSmoothing = textureSmoothing;
    }

    /** Gets antialiasing level. */
    public int getTextureSmoothing() {
        return textureSmoothing;
    }

    /**
     * Sets output file name.
     * @param outputFile valid file name without extension
     */
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    /** Gets output file name. */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * Sets whether generation time should be measured.
     * @param measureTime true to enable time measure, false to disable
     */
    public void setMeasureTime(boolean measureTime) {
        this.measureTime = measureTime;
    }

    /** Gets whether generation time should be measured. */
    public boolean getMeasureTime() {
        return measureTime;
    }

    /**
     * Generates a single video frame and saves it to a file .
     * @param pos current position in script
     * @param generator generator instance that should be used
     * @param frame frame index
     * @throws SemanticException When the output file name was already used.
     * @throws RuntimeException When an error occurs while saving the generated image.
     */
    public void generateFrame(CodePosition pos, Generator generator, int frame) throws SemanticException {
        var outFn = outputFile;
        var m = frameIdLocationPattern.matcher(outputFile);
        final boolean[] foundPosition = { false };
        outputFile = m.replaceAll(mr -> {
            foundPosition[0] = true;
            var targetLength = mr.group().length();
            var num = Integer.toString(frame);
            if (num.length() < targetLength) {
                return String.format("%0" + targetLength + "d", frame);
            } else {
                return num;
            }
        });
        if (!foundPosition[0]) {
            outputFile = outputFile + frame;
        }
        generateImage(pos, generator);
        outputFile = outFn;
    }

    /**
     * Generates an image and saves it to a file .
     * @param pos current position in script
     * @param generator generator instance that should be used
     * @throws SemanticException When the output file name was already used.
     * @throws RuntimeException When an error occurs while saving the generated image.
     */
    public void generateImage(CodePosition pos, Generator generator) throws SemanticException {
        generateImage(pos, generator, false);
    }

    /**
     * Generates an image and saves it to a file  only if the output file name was not used yet.
     * @param pos current position in script
     * @param generator generator instance that should be used
     * @throws SemanticException Never.
     * @throws RuntimeException When an error occurs while saving the generated image.
     */
    public void generateImageIgnoreDuplicates(CodePosition pos, Generator generator) throws SemanticException {
        generateImage(pos, generator, true);
    }

    /**
     * Generates an image and saves it to a file.
     * @param pos current position in script
     * @param generator generator instance that should be used
     * @param ignoreDuplicates determines behavior when the output file name was already used - if true, no image is
     *                         generated, otherwise an exception is thrown
     * @throws SemanticException When the output file name was already used and ignoreDuplicates is set to false.
     * @throws RuntimeException When an error occurs while saving the generated image.
     */
    private void generateImage(
        CodePosition pos,
        Generator generator,
        boolean ignoreDuplicates
    ) throws SemanticException {
        if (usedOutputFiles.contains(outputFile)) {
            if (ignoreDuplicates) {
                return;
            } else {
                // generating the same file repeatedly is not allowed
                throw new SemanticException(pos, "Can't generate multiple textures into one file");
            }
        }
        var time = measureTime ? System.currentTimeMillis() : 0;
        generator.init(textureSize, randomize);
        BufferedImage img = new BufferedImage(textureSize, textureSize, BufferedImage.TYPE_INT_ARGB);
        switch (textureSmoothing) {
            case 0:
                generatePixelatedImage(generator, img);
                break;
            case 1:
                generateSmoothImage(generator, img);
                break;
            default:
                generateSupersmoothImage(generator, img, textureSmoothing + 1);
                break;
        }
        var file = directory.resolve(outputFile + ".png").toFile();
        try {
            if (ImageIO.write(img, "png", file)) {
                if (measureTime) {
                    time = System.currentTimeMillis() - time;
                    var timeString = Utils.FormattedCurrentTime(time);
                    System.out.println("Generated file " + file + " in " + timeString);
                } else {
                    System.out.println("Generated file " + file);
                }
            } else {
                throw new RuntimeException("Unable to save texture to " + file);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Unable to save texture to " + file);
        }
        usedOutputFiles.add(outputFile);
    }

    /**
     * Generates an image with 1 sample per pixel.
     * @param generator generator instance that should be used
     * @param img target image
     */
    private void generatePixelatedImage(Generator generator, BufferedImage img) {
        RgbaColor color;
        for (int y = 0; y < textureSize; ++y) {
            double _y = (double)y / textureSize;
            for (int x = 0; x < textureSize; ++x) {
                color = generator.getColor((double)x / textureSize, _y);
                img.setRGB(x, y, color.toArgbPixel());
            }
        }
    }

    /**
     * Generates an image with 4 samples per pixel.
     * @param generator generator instance that should be used
     * @param img target image
     */
    private void generateSmoothImage(Generator generator, BufferedImage img) {
        RgbaColor c1, c2, c3, c4;
        double subpixelDistance = 0.5 / textureSize;
        for (int y = 0; y < textureSize; ++y) {
            double _y = (double)y / textureSize;
            for (int x = 0; x < textureSize; ++x) {
                double _x = (double)x / textureSize;
                c1 = generator.getColor(_x, _y);
                c2 = generator.getColor(_x + subpixelDistance, _y);
                c3 = generator.getColor(_x, _y + subpixelDistance);
                c4 = generator.getColor(_x + subpixelDistance, _y + subpixelDistance);
                img.setRGB(x, y, RgbaColor.average(c1, c2, c3, c4).toArgbPixel());
            }
        }
    }

    /**
     * Generates an image with a given number of samples per pixel.
     * @param generator generator instance that should be used
     * @param img target image
     * @param smoothingLevel number of samples per pixel is equal to smoothingLevel * smoothingLevel
     * @throws IllegalArgumentException When smoothingLevel \< 2.
     */
    private void generateSupersmoothImage(Generator generator, BufferedImage img, int smoothingLevel) {
        if (smoothingLevel < 2) {
            throw new IllegalArgumentException("smoothingLevel must be at least 2");
        }
        var cs = new RgbaColor[smoothingLevel * smoothingLevel];
        double subpixelDistance = 1.0 / (smoothingLevel * textureSize);
        for (int y = 0; y < textureSize; ++y) {
            double _y = (double)y / textureSize;
            for (int x = 0; x < textureSize; ++x) {
                double _x = (double)x / textureSize;
                for (var sx = 0; sx < smoothingLevel; ++sx) {
                    for (var sy = 0; sy < smoothingLevel; ++sy) {
                        cs[Utils.flatten(smoothingLevel, sx, sy)] = generator.getColor(
                            _x + subpixelDistance * sx,
                            _y + subpixelDistance * sy
                        );
                    }
                }
                img.setRGB(x, y, RgbaColor.average(cs).toArgbPixel());
            }
        }
    }
}
