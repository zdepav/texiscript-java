package cz.zdepav.school.texiscript.script.interpreter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.RgbaColor;
import cz.zdepav.school.texiscript.utils.SeededPositionedRandom;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/** @author Zdenek Pavlatka */
public class ImageRenderer {

    private static Pattern frameIdLocationPattern = Pattern.compile("#+");

    private boolean randomize, measureTime;
    private int textureSize, textureSmoothing;
    private String outputFile;
    private Set<String> usedOutputFiles;
    private Path directory;

    public ImageRenderer(Path directory, String outputFile) {
        this.directory = directory;
        randomize = false;
        measureTime = false;
        textureSize = 256;
        textureSmoothing = 1;
        this.outputFile = outputFile;
        usedOutputFiles = new HashSet<>();
    }

    public void setTextureSize(int textureSize) {
        this.textureSize = textureSize;
    }

    public int getTextureSize() {
        return textureSize;
    }

    public void setRandomization(boolean randomize) {
        this.randomize = randomize;
    }

    public boolean getRandomization() {
        return randomize;
    }

    public void setTextureSmoothing(int textureSmoothing) {
        this.textureSmoothing = textureSmoothing;
    }

    public int getTextureSmoothing() {
        return textureSmoothing;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setMeasureTime(boolean measureTime) {
        this.measureTime = measureTime;
    }

    public boolean getMeasureTime() {
        return measureTime;
    }

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

    public void generateImage(CodePosition pos, Generator generator) throws SemanticException {
        generateImage(pos, generator, false);
    }

    public void generateImageIgnoreDuplicates(CodePosition pos, Generator generator) throws SemanticException {
        generateImage(pos, generator, true);
    }

    private void generateImage(CodePosition pos, Generator generator, boolean ignoreDuplicates) throws SemanticException {
        if (usedOutputFiles.contains(outputFile)) {
            if (ignoreDuplicates) {
                return;
            } else {
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
            case 2:
                generateSupersmoothImage(generator, img);
                break;
        }
        var file = directory.resolve(outputFile + ".png").toFile();
        try {
            if (ImageIO.write(img, "png", file)) {
                if (measureTime) {
                    time = System.currentTimeMillis() - time;
                    var hours = time / 360000;
                    var minutes = time / 60000 % 60;
                    var seconds = time / 1000 % 60;
                    var miliseconds = time % 1000;
                    var timeString = new StringBuilder();
                    if (hours > 0) {
                        timeString.append(hours);
                        timeString.append("h ");
                    }
                    if (minutes > 0) {
                        timeString.append(minutes);
                        timeString.append("m ");
                    }
                    if (seconds > 0) {
                        timeString.append(seconds);
                        timeString.append("s ");
                    }
                    timeString.append(miliseconds);
                    timeString.append("ms");
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

    private void generateRandomImage(BufferedImage img) {
        RgbaColor color;
        for (int y = 0; y < textureSize; ++y) {
            double _y = (double)y / textureSize;
            for (int x = 0; x < textureSize; ++x) {
                var rand = new SeededPositionedRandom(textureSize, (double)x / textureSize, _y);
                rand.r();
                var r = rand.r();
                color = new RgbaColor(r);
                img.setRGB(x, y, color.toArgbPixel());
            }
        }
    }

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

    private void generateSupersmoothImage(Generator generator, BufferedImage img) {
        var cs = new RgbaColor[9];
        double subpixelDistance1 = 0.333 / textureSize;
        double subpixelDistance2 = 0.667 / textureSize;
        for (int y = 0; y < textureSize; ++y) {
            double _y = (double)y / textureSize;
            for (int x = 0; x < textureSize; ++x) {
                double _x = (double)x / textureSize;
                cs[0] = generator.getColor(_x, _y);
                cs[1] = generator.getColor(_x + subpixelDistance1, _y);
                cs[2] = generator.getColor(_x + subpixelDistance2, _y);
                cs[3] = generator.getColor(_x, _y + subpixelDistance1);
                cs[4] = generator.getColor(_x + subpixelDistance1, _y + subpixelDistance1);
                cs[5] = generator.getColor(_x + subpixelDistance2, _y + subpixelDistance1);
                cs[6] = generator.getColor(_x, _y + subpixelDistance2);
                cs[7] = generator.getColor(_x + subpixelDistance1, _y + subpixelDistance2);
                cs[8] = generator.getColor(_x + subpixelDistance2, _y + subpixelDistance2);
                img.setRGB(x, y, RgbaColor.average(cs).toArgbPixel());
            }
        }
    }
}
