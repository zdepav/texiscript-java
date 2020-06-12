package cz.zdepav.school.texiscript.script.runtime;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.curve.CubicBezierCurveGenerator;
import cz.zdepav.school.texiscript.generators.curve.QuadraticBezierCurveGenerator;
import cz.zdepav.school.texiscript.generators.video.FrameLengthGenerator;
import cz.zdepav.school.texiscript.generators.video.TimeGenerator;
import cz.zdepav.school.texiscript.generators.cellular.*;
import cz.zdepav.school.texiscript.generators.color.*;
import cz.zdepav.school.texiscript.generators.curve.CurveGenerator;
import cz.zdepav.school.texiscript.generators.gradients.*;
import cz.zdepav.school.texiscript.generators.math.*;
import cz.zdepav.school.texiscript.generators.mix.*;
import cz.zdepav.school.texiscript.generators.filter.*;
import cz.zdepav.school.texiscript.generators.perlin.*;
import cz.zdepav.school.texiscript.generators.shape.*;
import cz.zdepav.school.texiscript.generators.transform.*;
import cz.zdepav.school.texiscript.generators.video.TransitionGenerator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;
import cz.zdepav.school.texiscript.utils.Curve;
import cz.zdepav.school.texiscript.utils.RgbaColor;

import java.util.*;

/** Execution scope. */
public class Scope {

    /** all available packages (local package + standard library) */
    private final Map<String, Package> packages;

    /** currently imported packages */
    private final Map<String, Package> imported;

    /** Constructs a scope and initializes it with the standard library. */
    public Scope() {
        imported = new HashMap<>();
        packages = new HashMap<>();
        addLocalPackage();
        addCellularPackage();
        addColorPackage();
        addCurvePackage();
        addGradientPackage();
        addMathPackage();
        addMixPackage();
        addPerlinPackage();
        addPostprocessingPackage();
        addShapePackage();
        addTransformPackage();
        addVideoPackage();
    }

    /**
     * Imports a package.
     * @param pos current position in code
     * @param packageName name of package to import
     * @throws SemanticException When the requested package does not exist.
     */
    public void importPackage(CodePosition pos, String packageName) throws SemanticException {
        Package pack;
        if (packageName.equals("") || (pack = packages.getOrDefault(packageName, null)) == null) {
            throw new SemanticException(pos, "Package " + packageName + " does not exist.");
        } else imported.put(pack.name, pack);
    }

    /** Resets the scope to the state equal to constructing a new one. */
    public void reset() {
        imported.clear();
        packages.remove("");
        addLocalPackage();
    }

    /**
     * Gets value of a constant.
     * @param pos current position in the script
     * @param name variable name, can include package name
     * @return value of the requested constant
     * @throws SemanticException When the given name does not belong to a constant.
     */
    public Generator getConstant(CodePosition pos, String name) throws SemanticException {
        Generator value;
        int dotPos = name.lastIndexOf('.');
        if (dotPos >= 0) {
            // constants inside a package
            var packageName = name.substring(0, dotPos);
            name = name.substring(dotPos + 1);
            if (!packages.containsKey(packageName)) {
                throw new SemanticException(pos, "Package " + packageName + " does not exist.");
            }
            var pack = packages.get(packageName);
            if ((value = pack.getValue(name)) != null) {
                return value;
            } else
                throw new SemanticException(pos, "Constant " + name + " does not exist in package " + packageName + ".");
        } else if ((value = packages.get("").getValue(name)) != null) {
            // constant outside of any package (these take precedence before imported constants)
            return value;
        } else {
            // imported constants
            Generator g;
            String packageName = null;
            for (var pack: imported.entrySet()) {
                if ((g = pack.getValue().getValue(name)) != null) {
                    if (packageName != null) {
                        throw new SemanticException(
                            pos,
                            "Name conflict between " + packageName + '.' + name + " and " +
                                pack.getKey() + '.' + name + ", use qualified name instead."
                        );
                    } else {
                        packageName = pack.getKey();
                        value = g;
                    }
                }
            }
            if (value != null) {
                return value;
            }
        }
        // error - constant not found
        for (var pack: packages.entrySet()) {
            if (!pack.getKey().equals("") && pack.getValue().getValue(name) != null) {
                throw new SemanticException(
                    pos,
                    "Constant " + name + " does not exist. Did you mean " +
                        pack.getKey() + '.' + name + '?'
                );
            }
        }
        throw new SemanticException(pos, "Constant " + name + " does not exist.");
    }

    /**
     * Gets a function.
     * @param pos current position in the script
     * @param name function name, can include package name
     * @return requested function
     * @throws SemanticException When the given name does not belong to a function.
     */
    public Function getFunction(CodePosition pos, String name) throws SemanticException {
        Function func;
        int dotPos = name.lastIndexOf('.');
        if (dotPos >= 0) {
            // function inside a package
            var packageName = name.substring(0, dotPos);
            name = name.substring(dotPos + 1);
            if (!packages.containsKey(packageName)) {
                throw new SemanticException(pos, "Package " + packageName + " does not exist.");
            }
            var pack = packages.get(packageName);
            if ((func = pack.getFunc(name)) != null) {
                return func;
            } else {
                throw new SemanticException(
                    pos,
                    "Function " + name + " does not exist in package " + packageName + "."
                );
            }
        } else if ((func = packages.get("").getFunc(name)) != null) {
            // function outside of any package (these take precedence before imported functions)
            return func;
        } else {
            // imported function
            Function f;
            String packageName = null;
            for (var pack: imported.entrySet()) {
                if ((f = pack.getValue().getFunc(name)) != null) {
                    if (packageName != null) {
                        throw new SemanticException(
                            pos,
                            "Name conflict between " + packageName + '.' + name + " and " +
                                pack.getKey() + '.' + name + ", use qualified name instead."
                        );
                    } else {
                        packageName = pack.getKey();
                        func = f;
                    }
                }
            }
            if (func != null) {
                return func;
            }
        }
        // error - function not found
        for (var pack: packages.entrySet()) {
            if (!pack.getKey().equals("") && pack.getValue().getFunc(name) != null) {
                throw new SemanticException(
                    pos,
                    "Function " + name + " does not exist. Did you mean " +
                        pack.getKey() + '.' + name + '?'
                );
            }
        }
        throw new SemanticException(pos, "Function " + name + " does not exist.");
    }

    /**
     * Adds a variable / constant.
     * @param pos current position in the script
     * @param name variable name
     * @param value variable value
     * @throws SemanticException When name is not valid, contains a package name or does not belong to a constant.
     */
    public void createConstant(CodePosition pos, String name, Generator value) throws SemanticException {
        if (name.lastIndexOf('.') >= 0) {
            throw new SemanticException(pos, "Custom variables can't be placed into packages.");
        }
        var pack = packages.get("");
        if (pack.getValue(name) != null) {
            throw new SemanticException(pos, "Variable " + name + " already exists.");
        }
        pack.value(name, value);
    }

    /** Adds a package. */
    private void addPackage(Package pack) {
        packages.put(pack.name, pack);
    }

    /** Initializes package for local variables. */
    private void addLocalPackage() {
        addPackage(
            new Package("")
                .value("TRUE", Generator.get(1))
                .value("FALSE", Generator.get(0))
        );
    }

    /** Initializes package with cellular generators. */
    private void addCellularPackage() {
        addPackage(
            new Package("cellular")
                .func("cells", (pos, args) ->
                    CellularGenerator.build(pos, args, "cells", CellsCellularGenerator::new))
                .func("sparks", (pos, args) ->
                    CellularGenerator.build(pos, args, "sparks", SparksCellularGenerator::new))
                .func("cobble", (pos, args) ->
                    CellularGenerator.build(pos, args, "cobble", CobbleCellularGenerator::new))
                .func("mosaic", (pos, args) ->
                    CellularGenerator.build(pos, args, "mosaic", MosaicGenerator::new))
                .value("M_EUCLIDEAN", 0)
                .value("M_MANHATTAN", 1)
                .value("M_CHEBYSHEV", 2)
                .value("M_MINKOWSKI", 3)
        );
    }

    /** Initializes package with color manipulation functions. */
    private void addColorPackage() {
        addPackage(
            new Package("color")
                .func1("r", RedChannelGenerator::new)
                .func1("g", GreenChannelGenerator::new)
                .func1("b", BlueChannelGenerator::new)
                .func1("a", AlphaChannelGenerator::new)
                .func1("h", HueGenerator::new)
                .func1("s", SaturationGenerator::new)
                .func1("l", LightnessGenerator::new)
                .func3("rgb", RgbGenerator::new)
                .func4("rgba", RgbaGenerator::new)
                .func3("hsl", HslGenerator::new)
                .func4("hsla", HslaGenerator::new)
                .func2("setalpha", SetAlphaGenerator::new)
                .func1("grayscale", GrayscaleGenerator::new)
                .func3("noise", NoiseGenerator::new)
                .func1("lightinvert", SmartInvertGenerator::new)
                .func1("sepia", SepiaGenerator::new)
                .func0("black", p -> RgbaColor.black.generator())
                .func0("red", p -> RgbaColor.red.generator())
                .func0("green", p -> RgbaColor.green.generator())
                .func0("blue", p -> RgbaColor.blue.generator())
                .func0("yellow", p -> RgbaColor.yellow.generator())
                .func0("cyan", p -> RgbaColor.cyan.generator())
                .func0("magenta", p -> RgbaColor.magenta.generator())
                .func0("gray", p -> RgbaColor.gray.generator())
                .func0("white", p -> RgbaColor.white.generator())
                .func0("transparent", p -> RgbaColor.transparent.generator())
                .value("C_BLACK", RgbaColor.parseHex("#000000"))
                .value("C_NAVY", RgbaColor.parseHex("#000080"))
                .value("C_DARKBLUE", RgbaColor.parseHex("#00008B"))
                .value("C_MEDIUMBLUE", RgbaColor.parseHex("#0000CD"))
                .value("C_BLUE", RgbaColor.parseHex("#0000FF"))
                .value("C_DARKGREEN", RgbaColor.parseHex("#006400"))
                .value("C_GREEN", RgbaColor.parseHex("#008000"))
                .value("C_TEAL", RgbaColor.parseHex("#008080"))
                .value("C_DARKCYAN", RgbaColor.parseHex("#008B8B"))
                .value("C_DEEPSKYBLUE", RgbaColor.parseHex("#00BFFF"))
                .value("C_DARKTURQUOISE", RgbaColor.parseHex("#00CED1"))
                .value("C_MEDIUMSPRINGGREEN", RgbaColor.parseHex("#00FA9A"))
                .value("C_LIME", RgbaColor.parseHex("#00FF00"))
                .value("C_SPRINGGREEN", RgbaColor.parseHex("#00FF7F"))
                .value("C_AQUA", RgbaColor.parseHex("#00FFFF"))
                .value("C_CYAN", RgbaColor.parseHex("#00FFFF"))
                .value("C_MIDNIGHTBLUE", RgbaColor.parseHex("#191970"))
                .value("C_DODGERBLUE", RgbaColor.parseHex("#1E90FF"))
                .value("C_LIGHTSEAGREEN", RgbaColor.parseHex("#20B2AA"))
                .value("C_FORESTGREEN", RgbaColor.parseHex("#228B22"))
                .value("C_SEAGREEN", RgbaColor.parseHex("#2E8B57"))
                .value("C_DARKSLATEGRAY", RgbaColor.parseHex("#2F4F4F"))
                .value("C_DARKSLATEGREY", RgbaColor.parseHex("#2F4F4F"))
                .value("C_LIMEGREEN", RgbaColor.parseHex("#32CD32"))
                .value("C_MEDIUMSEAGREEN", RgbaColor.parseHex("#3CB371"))
                .value("C_TURQUOISE", RgbaColor.parseHex("#40E0D0"))
                .value("C_ROYALBLUE", RgbaColor.parseHex("#4169E1"))
                .value("C_STEELBLUE", RgbaColor.parseHex("#4682B4"))
                .value("C_DARKSLATEBLUE", RgbaColor.parseHex("#483D8B"))
                .value("C_MEDIUMTURQUOISE", RgbaColor.parseHex("#48D1CC"))
                .value("C_INDIGO", RgbaColor.parseHex("#4B0082"))
                .value("C_DARKOLIVEGREEN", RgbaColor.parseHex("#556B2F"))
                .value("C_CADETBLUE", RgbaColor.parseHex("#5F9EA0"))
                .value("C_CORNFLOWERBLUE", RgbaColor.parseHex("#6495ED"))
                .value("C_REBECCAPURPLE", RgbaColor.parseHex("#663399"))
                .value("C_MEDIUMAQUAMARINE", RgbaColor.parseHex("#66CDAA"))
                .value("C_DIMGRAY", RgbaColor.parseHex("#696969"))
                .value("C_DIMGREY", RgbaColor.parseHex("#696969"))
                .value("C_SLATEBLUE", RgbaColor.parseHex("#6A5ACD"))
                .value("C_OLIVEDRAB", RgbaColor.parseHex("#6B8E23"))
                .value("C_SLATEGRAY", RgbaColor.parseHex("#708090"))
                .value("C_SLATEGREY", RgbaColor.parseHex("#708090"))
                .value("C_LIGHTSLATEGRAY", RgbaColor.parseHex("#778899"))
                .value("C_LIGHTSLATEGREY", RgbaColor.parseHex("#778899"))
                .value("C_MEDIUMSLATEBLUE", RgbaColor.parseHex("#7B68EE"))
                .value("C_LAWNGREEN", RgbaColor.parseHex("#7CFC00"))
                .value("C_CHARTREUSE", RgbaColor.parseHex("#7FFF00"))
                .value("C_AQUAMARINE", RgbaColor.parseHex("#7FFFD4"))
                .value("C_MAROON", RgbaColor.parseHex("#800000"))
                .value("C_PURPLE", RgbaColor.parseHex("#800080"))
                .value("C_OLIVE", RgbaColor.parseHex("#808000"))
                .value("C_GRAY", RgbaColor.parseHex("#808080"))
                .value("C_GREY", RgbaColor.parseHex("#808080"))
                .value("C_SKYBLUE", RgbaColor.parseHex("#87CEEB"))
                .value("C_LIGHTSKYBLUE", RgbaColor.parseHex("#87CEFA"))
                .value("C_BLUEVIOLET", RgbaColor.parseHex("#8A2BE2"))
                .value("C_DARKRED", RgbaColor.parseHex("#8B0000"))
                .value("C_DARKMAGENTA", RgbaColor.parseHex("#8B008B"))
                .value("C_SADDLEBROWN", RgbaColor.parseHex("#8B4513"))
                .value("C_DARKSEAGREEN", RgbaColor.parseHex("#8FBC8F"))
                .value("C_LIGHTGREEN", RgbaColor.parseHex("#90EE90"))
                .value("C_MEDIUMPURPLE", RgbaColor.parseHex("#9370DB"))
                .value("C_DARKVIOLET", RgbaColor.parseHex("#9400D3"))
                .value("C_PALEGREEN", RgbaColor.parseHex("#98FB98"))
                .value("C_DARKORCHID", RgbaColor.parseHex("#9932CC"))
                .value("C_YELLOWGREEN", RgbaColor.parseHex("#9ACD32"))
                .value("C_SIENNA", RgbaColor.parseHex("#A0522D"))
                .value("C_BROWN", RgbaColor.parseHex("#A52A2A"))
                .value("C_DARKGRAY", RgbaColor.parseHex("#A9A9A9"))
                .value("C_DARKGREY", RgbaColor.parseHex("#A9A9A9"))
                .value("C_LIGHTBLUE", RgbaColor.parseHex("#ADD8E6"))
                .value("C_GREENYELLOW", RgbaColor.parseHex("#ADFF2F"))
                .value("C_PALETURQUOISE", RgbaColor.parseHex("#AFEEEE"))
                .value("C_LIGHTSTEELBLUE", RgbaColor.parseHex("#B0C4DE"))
                .value("C_POWDERBLUE", RgbaColor.parseHex("#B0E0E6"))
                .value("C_FIREBRICK", RgbaColor.parseHex("#B22222"))
                .value("C_DARKGOLDENROD", RgbaColor.parseHex("#B8860B"))
                .value("C_MEDIUMORCHID", RgbaColor.parseHex("#BA55D3"))
                .value("C_ROSYBROWN", RgbaColor.parseHex("#BC8F8F"))
                .value("C_DARKKHAKI", RgbaColor.parseHex("#BDB76B"))
                .value("C_SILVER", RgbaColor.parseHex("#C0C0C0"))
                .value("C_MEDIUMVIOLETRED", RgbaColor.parseHex("#C71585"))
                .value("C_INDIANRED", RgbaColor.parseHex("#CD5C5C"))
                .value("C_PERU", RgbaColor.parseHex("#CD853F"))
                .value("C_CHOCOLATE", RgbaColor.parseHex("#D2691E"))
                .value("C_TAN", RgbaColor.parseHex("#D2B48C"))
                .value("C_LIGHTGRAY", RgbaColor.parseHex("#D3D3D3"))
                .value("C_LIGHTGREY", RgbaColor.parseHex("#D3D3D3"))
                .value("C_THISTLE", RgbaColor.parseHex("#D8BFD8"))
                .value("C_ORCHID", RgbaColor.parseHex("#DA70D6"))
                .value("C_GOLDENROD", RgbaColor.parseHex("#DAA520"))
                .value("C_PALEVIOLETRED", RgbaColor.parseHex("#DB7093"))
                .value("C_CRIMSON", RgbaColor.parseHex("#DC143C"))
                .value("C_GAINSBORO", RgbaColor.parseHex("#DCDCDC"))
                .value("C_PLUM", RgbaColor.parseHex("#DDA0DD"))
                .value("C_BURLYWOOD", RgbaColor.parseHex("#DEB887"))
                .value("C_LIGHTCYAN", RgbaColor.parseHex("#E0FFFF"))
                .value("C_LAVENDER", RgbaColor.parseHex("#E6E6FA"))
                .value("C_DARKSALMON", RgbaColor.parseHex("#E9967A"))
                .value("C_VIOLET", RgbaColor.parseHex("#EE82EE"))
                .value("C_PALEGOLDENROD", RgbaColor.parseHex("#EEE8AA"))
                .value("C_LIGHTCORAL", RgbaColor.parseHex("#F08080"))
                .value("C_KHAKI", RgbaColor.parseHex("#F0E68C"))
                .value("C_ALICEBLUE", RgbaColor.parseHex("#F0F8FF"))
                .value("C_HONEYDEW", RgbaColor.parseHex("#F0FFF0"))
                .value("C_AZURE", RgbaColor.parseHex("#F0FFFF"))
                .value("C_SANDYBROWN", RgbaColor.parseHex("#F4A460"))
                .value("C_WHEAT", RgbaColor.parseHex("#F5DEB3"))
                .value("C_BEIGE", RgbaColor.parseHex("#F5F5DC"))
                .value("C_WHITESMOKE", RgbaColor.parseHex("#F5F5F5"))
                .value("C_MINTCREAM", RgbaColor.parseHex("#F5FFFA"))
                .value("C_GHOSTWHITE", RgbaColor.parseHex("#F8F8FF"))
                .value("C_SALMON", RgbaColor.parseHex("#FA8072"))
                .value("C_ANTIQUEWHITE", RgbaColor.parseHex("#FAEBD7"))
                .value("C_LINEN", RgbaColor.parseHex("#FAF0E6"))
                .value("C_LIGHTGOLDENRODYELLOW", RgbaColor.parseHex("#FAFAD2"))
                .value("C_OLDLACE", RgbaColor.parseHex("#FDF5E6"))
                .value("C_RED", RgbaColor.parseHex("#FF0000"))
                .value("C_FUCHSIA", RgbaColor.parseHex("#FF00FF"))
                .value("C_MAGENTA", RgbaColor.parseHex("#FF00FF"))
                .value("C_DEEPPINK", RgbaColor.parseHex("#FF1493"))
                .value("C_ORANGERED", RgbaColor.parseHex("#FF4500"))
                .value("C_TOMATO", RgbaColor.parseHex("#FF6347"))
                .value("C_HOTPINK", RgbaColor.parseHex("#FF69B4"))
                .value("C_CORAL", RgbaColor.parseHex("#FF7F50"))
                .value("C_DARKORANGE", RgbaColor.parseHex("#FF8C00"))
                .value("C_LIGHTSALMON", RgbaColor.parseHex("#FFA07A"))
                .value("C_ORANGE", RgbaColor.parseHex("#FFA500"))
                .value("C_LIGHTPINK", RgbaColor.parseHex("#FFB6C1"))
                .value("C_PINK", RgbaColor.parseHex("#FFC0CB"))
                .value("C_GOLD", RgbaColor.parseHex("#FFD700"))
                .value("C_PEACHPUFF", RgbaColor.parseHex("#FFDAB9"))
                .value("C_NAVAJOWHITE", RgbaColor.parseHex("#FFDEAD"))
                .value("C_MOCCASIN", RgbaColor.parseHex("#FFE4B5"))
                .value("C_BISQUE", RgbaColor.parseHex("#FFE4C4"))
                .value("C_MISTYROSE", RgbaColor.parseHex("#FFE4E1"))
                .value("C_BLANCHEDALMOND", RgbaColor.parseHex("#FFEBCD"))
                .value("C_PAPAYAWHIP", RgbaColor.parseHex("#FFEFD5"))
                .value("C_LAVENDERBLUSH", RgbaColor.parseHex("#FFF0F5"))
                .value("C_SEASHELL", RgbaColor.parseHex("#FFF5EE"))
                .value("C_CORNSILK", RgbaColor.parseHex("#FFF8DC"))
                .value("C_LEMONCHIFFON", RgbaColor.parseHex("#FFFACD"))
                .value("C_FLORALWHITE", RgbaColor.parseHex("#FFFAF0"))
                .value("C_SNOW", RgbaColor.parseHex("#FFFAFA"))
                .value("C_YELLOW", RgbaColor.parseHex("#FFFF00"))
                .value("C_LIGHTYELLOW", RgbaColor.parseHex("#FFFFE0"))
                .value("C_IVORY", RgbaColor.parseHex("#FFFFF0"))
                .value("C_WHITE", RgbaColor.parseHex("#FFFFFF"))
        );
    }

    /** Initializes package with curve generators. */
    private void addCurvePackage() {
        addPackage(
            new Package("curve")
                .func("linear", (pos, args) ->
                    CurveGenerator.build(pos, Curve.linear, "linear", args))
                .func("arc", (pos, args) ->
                    CurveGenerator.build(pos, Curve.arc, "arc", args))
                .func("invarc", (pos, args) ->
                    CurveGenerator.build(pos, Curve.invArc, "invarc", args))
                .func("doublearc", (pos, args) ->
                    CurveGenerator.build(pos, Curve.doubleArc, "doublearc", args))
                .func("invdoublearc", (pos, args) ->
                    CurveGenerator.build(pos, Curve.invDoubleArc, "invdoublearc", args))
                .func("sqr", (pos, args) ->
                    CurveGenerator.build(pos, Curve.sqr, "sqr", args))
                .func("sqrt", (pos, args) ->
                    CurveGenerator.build(pos, Curve.sqrt, "sqrt", args))
                .func("log", (pos, args) ->
                    CurveGenerator.build(pos, Curve.log, "log", args))
                .func("sin", (pos, args) ->
                    CurveGenerator.build(pos, Curve.sin, "sin", args))
                .func("quadratic", QuadraticBezierCurveGenerator::build)
                .func("bezier", CubicBezierCurveGenerator::build)
        );
    }

    /** Initializes package with gradient generators. */
    private void addGradientPackage() {
        addPackage(
            new Package("gradient")
                .func2("stop", GradientStopGenerator::new)
                .func("linear", LinearGradientGenerator::build)
                .func("radial", RadialGradientGenerator::build)
                .func("angular", AngularGradientGenerator::build)
        );
    }

    /** Initializes package with math functions. */
    private void addMathPackage() {
        addPackage(
            new Package("math")
                .func1("abs", AbsoluteValueGenerator::new)
                .func1("normalize", NormalizationGenerator::new)
                .func("add", BatchAdditionGenerator::build)
                .func2("sub", SubtractionGenerator::new)
                .func("mul", BatchMultiplicationGenerator::build)
                .func2("div", DivisionGenerator::new)
                .func1("sin", SineGenerator::new)
                .func1("cos", CosineGenerator::new)
                .func("random", RandomGenerator::build)
                .func1("invert", InvertingGenerator::new)
        );
    }

    /** Initializes package with combining generators. */
    private void addMixPackage() {
        addPackage(
            new Package("mix")
                .func2("blend", BlendGenerator::new)
                .func4("quattro", QuattroGenerator::new)
                .func4("sides", SidesGenerator::new)
                .func("choice", ChoiceGenerator::build)
                .func("switch", SwitchGenerator::build)
        );
    }

    /** Initializes package with perlin-based generators. */
    private void addPerlinPackage() {
        addPackage(
            new Package("perlin")
                .func("noise", (pos, args) ->
                    PerlinGenerator.build(pos, args, "noise", PerlinNoiseGenerator::new))
                .func("frost", (pos, args) ->
                    PerlinGenerator.build(pos, args, "frost", FrostedGlassGenerator::new))
                .func("camouflage", (pos, args) ->
                    PerlinGenerator.build(pos, args, "camouflage", CamouflageGenerator::new))
                .func("velvet", (pos, args) ->
                    PerlinGenerator.build(pos, args, "velvet", VelvetGenerator::new))
                .func("clouds", (pos, args) ->
                    PerlinGenerator.build(pos, args, "clouds", CloudsGenerator::new))
                .func("glass", (pos, args) ->
                    TurbulentPerlinGenerator.build(pos, args, "glass", GlassGenerator::new))
                .func("bark", (pos, args) ->
                    TurbulentPerlinGenerator.build(pos, args, "bark", BarkGenerator::new))
        );
    }

    /** Initializes package with filters. */
    private void addPostprocessingPackage() {
        addPackage(
            new Package("filter")
                .func("emboss", EmbosGenerator::build)
                .func("fastblur", FastBlurGenerator::build)
                .func("blur", BlurGenerator::build)
        );
    }

    /** Initializes package with shape generators. */
    private void addShapePackage() {
        addPackage(
            new Package("shape")
                .func("rect", RectangleGenerator::build)
                .func("circle", CircleGenerator::build)
                .func("ellipse", EllipseGenerator::build)
                .func("triangle", TriangleGenerator::build)
                .func("star", StarGenerator::build)
                .func10("itriangle", InterpolatedTriangleGenerator::new)
        );
    }

    /** Initializes package with transforming functions. */
    private void addTransformPackage() {
        addPackage(
            new Package("transform")
                .func5("fisheye", FisheyeGenerator::new)
                .func3("polar", PolarGenerator::new)
                .func3("depolar", DepolarGenerator::new)
                .func3("offset", OffsetGenerator::new)
                .func3("translate", TranslationGenerator::new)
                .func4("rotate", RotationGenerator::new)
                .func1("rrot", RightRotationGenerator::new)
                .func1("hrot", HalfRotationGenerator::new)
                .func1("lrot", LeftRotationGenerator::new)
                .func1("flipx", FlipXGenerator::new)
                .func1("flipy", FlipYGenerator::new)
                .func("scale", ScaleGenerator::build)
                .func("tiles", TilesGenerator::build)
        );
    }

    /** Initializes package with video-related functions. */
    private void addVideoPackage() {
        addPackage(
            new Package("video")
                .func0("time", TimeGenerator::new)
                .func0("frame", FrameLengthGenerator::new)
                .func("transition", TransitionGenerator::build)
        );
    }

    /**
     * Iterates over all local constants (variables).
     * @return Iterable with name-value pairs
     */
    public Iterable<Map.Entry<String, Generator>> listLocalVariables() {
        var map = new TreeMap<String, Generator>();
        for (var entry: packages.get("").listValues()) {
            if (!entry.getKey().equals("TRUE") && !entry.getKey().equals("FALSE")) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
        return map.entrySet();
    }
}
