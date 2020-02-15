package cz.zdepav.school.texiscript.utils;

import cz.zdepav.school.texiscript.generators.Generator;

/** @author Zdenek Pavlatka */
public class RgbaColor {

    public final double r, g, b, a, intensity;
    private Generator gen;

    public RgbaColor(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = Utils.clamp(a, 0, 1);
        intensity = (this.r + this.g + this.b) / 3;
        gen = null;
    }

    public RgbaColor(double r, double g, double b) { this(r, g, b, 1); }

    public RgbaColor(double intensity) {
        this.r = intensity;
        this.g = intensity;
        this.b = intensity;
        this.a = 1;
        this.intensity = intensity;
        gen = null;
    }

    /**
     * Parses color from string in css hex format, possible formats are: #RGB, #RGBA, #RRGGBB, #RRGGBBAA
     * @param str string to parse
     * @return parsed color
     * @throws IllegalArgumentException when the string is null or is not in a valid format
     */
    public static RgbaColor parseHex(String str) {
        if (str == null) {
            throw new IllegalArgumentException("str can't be null");
        }
        str = str.toLowerCase();
        if (str.matches("^#[0-9a-f]{3}[0-9a-f]?$")) {
            return new RgbaColor(
                Integer.parseInt(str.substring(1, 2), 16) / 15.0,
                Integer.parseInt(str.substring(2, 3), 16) / 15.0,
                Integer.parseInt(str.substring(3, 4), 16) / 15.0,
                str.length() > 4 ? Integer.parseInt(str.substring(4, 5), 16) / 15.0 : 1
            );
        } else if (str.matches("^#[0-9a-f]{6}([0-9a-f]{2})?$")) {
            return new RgbaColor(
                Integer.parseInt(str.substring(1, 3), 16) / 255.0,
                Integer.parseInt(str.substring(3, 5), 16) / 255.0,
                Integer.parseInt(str.substring(5, 7), 16) / 255.0,
                str.length() > 7 ? Integer.parseInt(str.substring(7, 9), 16) / 255.0 : 1
            );
        } else throw new IllegalArgumentException("Invalid color format");
    }

    public static RgbaColor average(RgbaColor c1, RgbaColor c2, RgbaColor c3, RgbaColor c4) {
        return new RgbaColor(
            (c1.r + c2.r + c3.r + c4.r) * 0.25,
            (c1.g + c2.g + c3.g + c4.g) * 0.25,
            (c1.b + c2.b + c3.b + c4.b) * 0.25,
            (c1.a + c2.a + c3.a + c4.a) * 0.25
        );
    }

    public static RgbaColor average(RgbaColor[] colors) {
        double r = 0.0, g = 0.0, b = 0.0, a = 0.0;
        for (int i = 0; i < colors.length; ++i) {
            var c = colors[i];
            r += c.r;
            g += c.g;
            b += c.b;
            a += c.a;
        }
        return new RgbaColor(r / colors.length, g / colors.length, b / colors.length, a / colors.length);
    }

    public RgbaColor multiply(double ammount, boolean multiplyAlpha) {
        return new RgbaColor(r * ammount, g * ammount, b * ammount, multiplyAlpha ? a * ammount : a);
    }

    public RgbaColor multiply(double ammount) {
        return new RgbaColor(r * ammount, g * ammount, b * ammount, a);
    }

    public RgbaColor multiply(RgbaColor c) {
        return new RgbaColor(r * c.r, g * c.g, b * c.b, a * c.a);
    }

    public RgbaColor divide(double ammount, boolean divideAlpha) {
        return new RgbaColor(r / ammount, g / ammount, b / ammount, divideAlpha ? a / ammount : a);
    }

    public RgbaColor divide(double ammount) {
        return new RgbaColor(r / ammount, g / ammount, b / ammount, a);
    }

    public RgbaColor divide(RgbaColor c) {
        return new RgbaColor(r / c.r, g / c.g, b / c.b, a / c.a);
    }

    public RgbaColor add(RgbaColor c) {
        return new RgbaColor(r + c.r * c.a, g + c.g * c.a, b + c.b * c.a, a + c.a * c.a);
    }

    public RgbaColor add(double ammount) {
        return new RgbaColor(r + ammount, g + ammount, b + ammount, a + ammount);
    }

    public RgbaColor sub(RgbaColor c) {
        return new RgbaColor(r - c.r * c.a, g - c.g * c.a, b - c.b * c.a, a + c.a * c.a);
    }

    public RgbaColor sub(double ammount) {
        return new RgbaColor(r - ammount, g - ammount, b - ammount, a - ammount);
    }

    public RgbaColor blend(RgbaColor c) {
        if (this.a < 0.001) {
            return c.a < 0.001 ? this : c;
        } else if (c.a < 0.001) {
            return this;
        } else {
            var ra = 1 - c.a;
            return new RgbaColor(
                r * ra + r * a,
                g * ra + g * a,
                b * ra + b * a,
                a + c.a * (1 - a)
            );
        }
    }

    public RgbaColor withRed(double r) { return new RgbaColor(r, this.g, this.b, this.a); }

    public RgbaColor withGreen(double g) { return new RgbaColor(this.r, g, this.b, this.a); }

    public RgbaColor withBlue(double b) { return new RgbaColor(this.r, this.g, b, this.a); }

    public RgbaColor withAlpha(double a) { return new RgbaColor(this.r, this.g, this.b, a); }

    public RgbaColor lerp(RgbaColor c, double ammount) {
        if (ammount >= 1) {
            return c;
        } else if (ammount <= 0) {
            return this;
        } else {
            var a2 = 1 - ammount;
            return new RgbaColor(
                r * a2 + c.r * ammount,
                g * a2 + c.g * ammount,
                b * a2 + c.b * ammount,
                a * a2 + c.a * ammount
            );
        }
    }

    public RgbaColor addNoise(double intensity, double saturation) {
        return addNoise(intensity, saturation, Rand.INSTANCE);
    }

    public RgbaColor addNoise(double intensity, double saturation, RandomGenerator rand) {
        if (saturation <= 0) {
            var n = rand.r(-intensity, intensity);
            return new RgbaColor(r + n, g + n, b + n, a);
        } else if (saturation >= 1) {
            return new RgbaColor(
                this.r + rand.r(-intensity, intensity),
                this.g + rand.r(-intensity, intensity),
                this.b + rand.r(-intensity, intensity),
                this.a
            );
        } else {
            var s2 = 1 - saturation;
            var rn = rand.r(-intensity, intensity);
            var gn = saturation * rand.r(-intensity, intensity) + s2 * rn;
            var bn = saturation * rand.r(-intensity, intensity) + s2 * rn;
            return new RgbaColor(r + rn, g + gn, b + bn, a);
        }
    }

    public RgbaColor negate() {
        return new RgbaColor(1.0 - r, 1.0 - g, 1.0 - b, a);
    }

    public RgbaColor normalize() {
        return new RgbaColor(
            Utils.clamp(r, 0, 1),
            Utils.clamp(g, 0, 1),
            Utils.clamp(b, 0, 1),
            a
        );
    }

    public RgbaColor abs() {
        return new RgbaColor(Math.abs(r), Math.abs(g), Math.abs(b), a);
    }

    public Generator generator() {
        if (gen == null) {
            gen = Generator.get(this);
        }
        return gen;
    }

    @Override
    public String toString() {
        return "#" +
            Utils.byteToHex((int)Math.round(r * 255.0)) +
            Utils.byteToHex((int)Math.round(g * 255.0)) +
            Utils.byteToHex((int)Math.round(b * 255.0)) +
            Utils.byteToHex((int)Math.round(a * 255.0));
    }

    public int getAByte() {
        return Utils.clamp((int)Math.round(a * 255.0), 0, 255);
    }

    public int getRByte() {
        return Utils.clamp((int)Math.round(r * 255.0), 0, 255);
    }

    public int getGByte() {
        return Utils.clamp((int)Math.round(g * 255.0), 0, 255);
    }

    public int getBByte() {
        return Utils.clamp((int)Math.round(b * 255.0), 0, 255);
    }

    public int toArgbPixel() {
        return (getAByte() << 24) | (getRByte() << 16) | (getGByte() << 8) | getBByte();
    }

    public static RgbaColor fromArgbPixel(int pixel) {
        return new RgbaColor(
            ((pixel >> 16) & 255) / 255.0,
            ((pixel >> 8) & 255) / 255.0,
            (pixel & 255) / 255.0,
            ((pixel >> 24) & 255) / 255.0
        );
    }

    public static final RgbaColor
        transparent = new RgbaColor(0, 0, 0, 0),
        black = new RgbaColor(0),
        red = new RgbaColor(1, 0, 0),
        green = new RgbaColor(0, 1, 0),
        blue = new RgbaColor(0, 0, 1),
        yellow = new RgbaColor(1, 1, 0),
        cyan = new RgbaColor(0, 1, 1),
        magenta = new RgbaColor(1, 0, 1),
        white = new RgbaColor(1),
        gray = new RgbaColor(0.5);
}
