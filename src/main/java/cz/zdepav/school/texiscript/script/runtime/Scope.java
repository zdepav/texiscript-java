package cz.zdepav.school.texiscript.script.runtime;

import cz.zdepav.school.texiscript.generators.Generator;
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

/** @author Zdenek Pavlatka */
public class Scope {

    private Map<String, Package> packages, imported;

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

    public void importPackage(CodePosition pos, String packageName) throws SemanticException {
        Package pack;
        if (packageName.equals("") || (pack = packages.getOrDefault(packageName, null)) == null) {
            throw new SemanticException(pos, "Package " + packageName + " does not exist.");
        } else imported.put(pack.name, pack);
    }

    public void reset() {
        imported.clear();
        packages.remove("");
        addLocalPackage();
    }

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

    private void addPackage(Package pack) {
        packages.put(pack.name, pack);
    }

    private void addLocalPackage() {
        addPackage(
            new Package("")
                .value("TRUE", Generator.get(1))
                .value("FALSE", Generator.get(0))
        );
    }

    private void addCellularPackage() {
        addPackage(
            new Package("cellular")
                .func("cells", (pos, args) ->
                    CellularGenerator.build(pos, args, "cells", CellsCellularGenerator::new))
                .func("sparks", (pos, args) ->
                    CellularGenerator.build(pos, args, "sparks", SparksCellularGenerator::new))
                .func("cobble", (pos, args) ->
                    CellularGenerator.build(pos, args, "cobble", CobbleCellularGenerator::new))
                .func("mosaic", MosaicGenerator::build)
                .value("M_EUCLIDEAN", 0)
                .value("M_MANHATTAN", 1)
                .value("M_CHEBYSHEV", 2)
                .value("M_MINKOWSKI", 3)
        );
    }

    private void addColorPackage() {
        addPackage(
            new Package("color")
                .func1("r", RedChannelGenerator::new)
                .func1("g", GreenChannelGenerator::new)
                .func1("b", BlueChannelGenerator::new)
                .func1("a", AlphaChannelGenerator::new)
                .func3("rgb", RgbGenerator::new)
                .func4("rgba", RgbaGenerator::new)
                .func3("hsl", HslGenerator::new)
                .func4("hsla", HslaGenerator::new)
                .func2("setalpha", SetAlphaGenerator::new)
                .func1("grayscale", GrayscaleGenerator::new)
                .func3("noise", NoiseGenerator::new)
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
        );
    }

    private void addCurvePackage() {
        addPackage(
            new Package("curve")
                .func("linear", (pos, args) ->
                    CurveGenerator.build(pos, Curve.linear, "linear", args))
                .func("arc", (pos, args) ->
                    CurveGenerator.build(pos, Curve.arc, "arc", args))
                .func("invarc", (pos, args) ->
                    CurveGenerator.build(pos, Curve.invArc, "invarc", args))
                .func("sqr", (pos, args) ->
                    CurveGenerator.build(pos, Curve.sqr, "sqr", args))
                .func("sqrt", (pos, args) ->
                    CurveGenerator.build(pos, Curve.sqrt, "sqrt", args))
                .func("log", (pos, args) ->
                    CurveGenerator.build(pos, Curve.log, "log", args))
                .func("sin", (pos, args) ->
                    CurveGenerator.build(pos, Curve.sin, "sin", args))
        );
    }

    private void addGradientPackage() {
        addPackage(
            new Package("gradient")
                .func2("stop", GradientStopGenerator::new)
                .func("linear", LinearGradientGenerator::build)
                .func("radial", RadialGradientGenerator::build)
                .func("angular", AngularGradientGenerator::build)
        );
    }

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
                .func1("random", RandomGenerator::new)
                .func1("invert", InvertingGenerator::new)
        );
    }

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

    private void addPostprocessingPackage() {
        addPackage(
            new Package("filter")
                .func("emboss", EmbosGenerator::build)
                .func("fastblur", FastBlurGenerator::build)
                .func("blur", BlurGenerator::build)
        );
    }

    private void addShapePackage() {
        addPackage(
            new Package("shape")
                .func6("rect", RectangleGenerator::new)
                .func5("circle", CircleGenerator::new)
                .func6("ellipse", EllipseGenerator::new)
                .func8("triangle", TriangleGenerator::new)
        );
    }

    private void addTransformPackage() {
        addPackage(
            new Package("transform")
                .func5("fisheye", FisheyeGenerator::new)
                .func3("polar", PolarGenerator::new)
                .func3("depolar", DepolarGenerator::new)
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

    private void addVideoPackage() {
        addPackage(
            new Package("video")
                .func0("time", TimeGenerator::new)
                .func("transition", TransitionGenerator::build)
        );
    }

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
