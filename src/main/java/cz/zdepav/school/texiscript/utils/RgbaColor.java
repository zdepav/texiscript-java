package cz.zdepav.school.texiscript.utils;

import cz.zdepav.school.texiscript.generators.Generator;

/** Immutable representation of RGBA color. */
public class RgbaColor {

    /** red component */
    public final double r;

    /** green component */
    public final double g;

    /** blue component */
    public final double b;

    /** alpha (opacity) component */
    public final double a;

    /** color intensity (value) */
    public final double intensity;

    /** ColorGenerator cache for this color, only not-null if generator() was called before .*/
    private Generator gen;

    /**
     * Creates instance with given r, g, b, a.
     * @param r red component
     * @param g green component
     * @param b blue component
     * @param a alpha (opacity) component
     */
    public RgbaColor(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = Utils.clamp(a, 0, 1);
        intensity = (this.r + this.g + this.b) / 3;
        gen = null;
    }

    /**
     * Creates instance with given r, g, b. Alpha is set to 1.
     * @param r red component
     * @param g green component
     * @param b blue component
     */
    public RgbaColor(double r, double g, double b) { this(r, g, b, 1); }

    /**
     * Creates instance with r, g, b equal to x, y, z properties of the rgb vector. Alpha is set to 1.
     * @param rgb vector with values for red, green and blue components
     */
    public RgbaColor(Vec3 rgb) { this(rgb.x, rgb.y, rgb.z, 1); }

    /**
     * Creates instance with r, g, b equal to intensity. Alpha is set to 1.
     * @param intensity intensity value
     */
    public RgbaColor(double intensity) {
        this.r = intensity;
        this.g = intensity;
        this.b = intensity;
        this.a = 1;
        this.intensity = intensity;
        gen = null;
    }

    /**
     * Parses color from string in css hex format, possible formats are: \#RGB, \#RGBA, \#RRGGBB, \#RRGGBBAA.
     * @param str String to parse.
     * @return Parsed color.
     * @throws IllegalArgumentException When the string is null or is not in a valid format.
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

    /**
     * Computes average of 4 colors.
     * @param c1 First color.
     * @param c2 Second color.
     * @param c3 Third color.
     * @param c4 Fourth color.
     * @return Average of the given colors.
     */
    public static RgbaColor average(RgbaColor c1, RgbaColor c2, RgbaColor c3, RgbaColor c4) {
        return new RgbaColor(
            (c1.r + c2.r + c3.r + c4.r) * 0.25,
            (c1.g + c2.g + c3.g + c4.g) * 0.25,
            (c1.b + c2.b + c3.b + c4.b) * 0.25,
            (c1.a + c2.a + c3.a + c4.a) * 0.25
        );
    }

    /**
     * Computes average of given colors.
     * @param colors Colors to compute average of.
     * @return Average of the given colors.
     */
    public static RgbaColor average(RgbaColor[] colors) {
        if (colors == null) {
            return null;
        }
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

    /**
     * Multiplies this color by a given value and returns the new color.
     * @param ammount value to multiply by
     * @param multiplyAlpha when false, alpha components will be left intact
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor multiply(double ammount, boolean multiplyAlpha) {
        return new RgbaColor(r * ammount, g * ammount, b * ammount, multiplyAlpha ? a * ammount : a);
    }

    /**
     * Multiplies this color by a given value and returns the result.
     * @param ammount value to multiply by
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor multiply(double ammount) {
        return new RgbaColor(r * ammount, g * ammount, b * ammount, a);
    }

    /**
     * Multiplies this color by another color (each component separately) and returns the result.
     * @param c color to multiply by
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor multiply(RgbaColor c) {
        return new RgbaColor(r * c.r, g * c.g, b * c.b, a * c.a);
    }

    /**
     * Divides this color by a given value and returns the new color.
     * @param ammount value to divide by
     * @param divideAlpha when false, alpha components will be left intact
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor divide(double ammount, boolean divideAlpha) {
        return new RgbaColor(r / ammount, g / ammount, b / ammount, divideAlpha ? a / ammount : a);
    }

    /**
     * Divides this color by a given value and returns the result.
     * @param ammount value to divide by
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor divide(double ammount) {
        return new RgbaColor(r / ammount, g / ammount, b / ammount, a);
    }

    /**
     * Divides this color by another color (each component separately) and returns the result.
     * @param c color to divide by
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor divide(RgbaColor c) {
        return new RgbaColor(r / c.r, g / c.g, b / c.b, a / c.a);
    }

    /**
     * Adds another color to this one and returns the result. The added color will be multiplied by it's alpha
     * @param c color to add
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor add(RgbaColor c) {
        return new RgbaColor(r + c.r * c.a, g + c.g * c.a, b + c.b * c.a, a + c.a * c.a);
    }

    /**
     * Adds a value to all components of this color and returns the result.
     * @param ammount value to add
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor add(double ammount) {
        return new RgbaColor(r + ammount, g + ammount, b + ammount, a + ammount);
    }

    /**
     * Subtracts another color from this one and returns the result. The added color will be multiplied by it's alpha
     * @param c color to subtract
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor sub(RgbaColor c) {
        return new RgbaColor(r - c.r * c.a, g - c.g * c.a, b - c.b * c.a, a + c.a * c.a);
    }

    /**
     * Subtracts a value from all components of this color and returns the result.
     * @param ammount value to subtract
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor sub(double ammount) {
        return new RgbaColor(r - ammount, g - ammount, b - ammount, a - ammount);
    }

    /**
     * Blends another color onto this one using alpha blending and returns the result.
     * @param c color to add
     * @return New RgbaColor instance with the result.
     */
    public RgbaColor blend(RgbaColor c) {
        if (a < 0.001) {
            return c.a < 0.001 ? this : c;
        } else if (c.a < 0.001) {
            return this;
        } else {
            var ra = 1 - c.a;
            return new RgbaColor(
                r * ra + c.r * c.a,
                g * ra + c.g * c.a,
                b * ra + c.b * c.a,
                a + c.a * (1 - a)
            );
        }
    }

    /**
     * Clones this color and replaces the red component by the given value.
     * @param r new value for the red component
     * @return New RgbaColor instance with red component set to the given value and green, blue and alpha components
     *         copied from this color.
     */
    public RgbaColor withRed(double r) { return new RgbaColor(r, this.g, this.b, this.a); }

    /**
     * Clones this color and replaces the green component by the given value.
     * @param g new value for the green component
     * @return New RgbaColor instance with green component set to the given value and red, blue and alpha components
     *         copied from this color.
     */
    public RgbaColor withGreen(double g) { return new RgbaColor(this.r, g, this.b, this.a); }

    /**
     * Clones this color and replaces the blue component by the given value.
     * @param b new value for the blue component
     * @return New RgbaColor instance with blue component set to the given value and red, green and alpha components
     *         copied from this color.
     */
    public RgbaColor withBlue(double b) { return new RgbaColor(this.r, this.g, b, this.a); }

    /**
     * Clones this color and replaces the alpha component by the given value.
     * @param a new value for the alpha component
     * @return New RgbaColor instance with alpha component set to the given value and red, green and blue components
     *         copied from this color.
     */
    public RgbaColor withAlpha(double a) { return new RgbaColor(this.r, this.g, this.b, a); }

    /**
     * Interpolates between this color and another one using linear interpolation.
     * @param c second color to use
     * @param ammount fraction between the two colors (0 ~ this color, 1 ~ other color)
     * @return New RgbaColor instance with the resulting color.
     */
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

    /**
     * Adds random value to this color and returns the result.
     * @param intensity how much can the new color differ from this one
     * @param saturation greater values of saturation allow for different color components to be changed differently
     *                   (0 ~ all components will have the same value added to them,
     *                    1 ~ all components will be modified independently)
     * @return New RgbaColor instance with the modified color.
     */
    public RgbaColor addNoise(double intensity, double saturation) {
        return addNoise(intensity, saturation, Rand.INSTANCE);
    }

    /**
     * Adds random value generateg using the given random generator to this color and returns the result.
     * @param intensity how much can the new color differ from this one
     * @param saturation greater values of saturation allow for different color components to be changed differently
     *                   (0 ~ all components will have the same value added to them,
     *                    1 ~ all components will be modified independently)
     * @param rand random generator that should be used
     * @return New RgbaColor instance with the modified color.
     */
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

    /**
     * Negates (inverts) the red, green and blue components of this color and returns the result.
     * @return New RgbaColor instance with red, green and blue components negated.
     */
    public RgbaColor negate() {
        return new RgbaColor(1.0 - r, 1.0 - g, 1.0 - b, a);
    }

    /**
     * Inverts lightness while keeping hue and saturation and returns the result.
     * @return New RgbaColor instance with negated lightness.
     */
    public RgbaColor smartNegate() {
        var max = Math.max(r, Math.max(g, b));
        var min = Math.min(r, Math.min(g, b));
        var sub = min + max - 1.0;
        return new RgbaColor(r - sub, g - sub, b - sub, a);
    }

    /**
     * Clamps all color components between 0 and 1 and returns the result.
     * @return Normalized RgbaColor instance.
     */
    public RgbaColor normalize() {
        return new RgbaColor(
            Utils.clamp(r, 0, 1),
            Utils.clamp(g, 0, 1),
            Utils.clamp(b, 0, 1),
            a
        );
    }

    /** Computes hue component of this color. */
    public double hue() {
        var delta = Math.max(r, Math.max(g, b)) - Math.min(r, Math.min(g, b));
        if (Math.abs(delta) < 0.00001) {
            return 0.0;
        } else if (r >= g && r >= b) {
            return (((g - b) / delta + 6) % 6) / 6;
        } else if (g >= r && g >= b) {
            return ((b - r) / delta + 2) / 6;
        } else {
            return ((r - g) / delta + 4) / 6;
        }
    }

    /** Computes saturation component of this color. */
    public double saturation() {
        var cmax = Math.max(r, Math.max(g, b));
        var cmin = Math.min(r, Math.min(g, b));
        var delta = cmax - cmin;
        if (Math.abs(delta) < 0.00001) {
            return 0.0;
        } else {
            return delta / (1.0 - Math.abs(cmax + cmin - 1.0));
        }
    }

    /** Computes lightness component of this color. */
    public double lightness() {
        return (Math.max(r, Math.max(g, b)) + Math.min(r, Math.min(g, b))) / 2;
    }

    /** Computes absolute value of this color. */
    public RgbaColor abs() {
        return new RgbaColor(Math.abs(r), Math.abs(g), Math.abs(b), a);
    }

    /** Returns ColorGenerator for this color. */
    public Generator generator() {
        if (gen == null) {
            gen = Generator.get(this);
        }
        return gen;
    }

    /** Converts this color to a string in the format \#rrggbbaa. */
    @Override
    public String toString() {
        return "#" +
            Utils.byteToHex((int)Math.round(r * 255.0)) +
            Utils.byteToHex((int)Math.round(g * 255.0)) +
            Utils.byteToHex((int)Math.round(b * 255.0)) +
            Utils.byteToHex((int)Math.round(a * 255.0));
    }

    /** Converts the alpha component to a byte. */
    public int getAByte() {
        return Utils.clamp((int)Math.round(a * 255.0), 0, 255);
    }

    /** Converts the red component to a byte. */
    public int getRByte() {
        return Utils.clamp((int)Math.round(r * 255.0), 0, 255);
    }

    /** Converts the green component to a byte. */
    public int getGByte() {
        return Utils.clamp((int)Math.round(g * 255.0), 0, 255);
    }

    /** Converts the blue component to a byte. */
    public int getBByte() {
        return Utils.clamp((int)Math.round(b * 255.0), 0, 255);
    }

    /** Converts this color to a single integer value. */
    public int toArgbPixel() {
        return (getAByte() << 24) | (getRByte() << 16) | (getGByte() << 8) | getBByte();
    }

    /**
     * Converts integer valueto a color.
     * @param pixel value to convert
     * @return New RgbaColor instance created from the given integer.
     */
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
