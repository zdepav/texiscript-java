package cz.zdepav.school.texiscript.script.interpreter;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.generators.video.FrameLengthGenerator;
import cz.zdepav.school.texiscript.generators.video.TimeGenerator;
import cz.zdepav.school.texiscript.script.parser.Lexer;
import cz.zdepav.school.texiscript.script.parser.Parser;
import cz.zdepav.school.texiscript.script.parser.SyntaxException;
import cz.zdepav.school.texiscript.script.parser.TokenSource;
import cz.zdepav.school.texiscript.script.runtime.Scope;
import cz.zdepav.school.texiscript.script.syntaxtree.StAssignment;
import cz.zdepav.school.texiscript.script.syntaxtree.StColor;
import cz.zdepav.school.texiscript.script.syntaxtree.StCommand;
import cz.zdepav.school.texiscript.script.syntaxtree.StCommandArgument;
import cz.zdepav.school.texiscript.script.syntaxtree.StFunctionCall;
import cz.zdepav.school.texiscript.script.syntaxtree.StGenerator;
import cz.zdepav.school.texiscript.script.syntaxtree.StNode;
import cz.zdepav.school.texiscript.script.syntaxtree.StNumber;
import cz.zdepav.school.texiscript.script.syntaxtree.StString;
import cz.zdepav.school.texiscript.script.syntaxtree.StType;
import cz.zdepav.school.texiscript.script.syntaxtree.StVariable;
import cz.zdepav.school.texiscript.utils.Utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Pattern;

/** Basic script interpreter. */
public class Interpreter {

    /** regexp used to extract file name from path */
    private static final Pattern fileNamePattern = Pattern.compile("^(?:.*[/\\\\])*([^\\\\/]+?)(?:\\.[^./\\\\]+)?$");

    /** parsed script */
    private List<StNode> script;

    /** current execution scope */
    private final Scope scope;

    /** working directory */
    private final Path directory;

    /** token source (lexer) used to read the script */
    private final TokenSource tokenSource;

    /** image renderer used to generate images */
    private final ImageRenderer imageRenderer;

    /**
     * Constructs an interpreter.
     * @param directory working directory
     * @param scriptFileName name of the script file that will be executed
     * @param input input stream to read the script from
     */
    public Interpreter(Path directory, String scriptFileName, InputStream input) {
        this.directory = directory;
        this.tokenSource = new Lexer(input);
        script = null;
        scope = new Scope();
        var m = fileNamePattern.matcher(scriptFileName);
        imageRenderer = new ImageRenderer(directory, m.find() ? m.group(1) : "tex");
    }

    /**
     * Parses the entire script in advance.
     * @throws SyntaxException When the script contains errors.
     */
    public void precompile() throws SyntaxException {
        if (script != null) {
            return;
        }
        var parser = new Parser(tokenSource);
        script = parser.parseScript();
    }

    /**
     * Parses the script (unless it was already parsed) and executes it.
     * @throws SyntaxException When the script contains errors.
     * @throws SemanticException When the script contains errors.
     */
    public void execute() throws SyntaxException, SemanticException {
        if (script == null) {
            precompile();
        }
        scope.reset();
        for (var node: script) {
            if (node instanceof StAssignment) {
                var assign = (StAssignment)node;
                scope.createConstant(
                    assign.getCodePosition(),
                    assign.getVariableName(),
                    buildGenerator(assign.getValue())
                );
            } else if (node instanceof StCommand) {
                executeCommand((StCommand)node);
            }
        }
    }

    /**
     * Helper method for exception construction.
     * @param badNode syntax tree node that caused the error
     * @param message error message
     * @return the constructed exception
     */
    private SemanticException error(StNode badNode, String message) {
        return new SemanticException(badNode.getCodePosition(), message);
    }

    /**
     * Constructs a generator instance from a syntax tree node.
     * @param expression syntax tree node to interpret
     * @return the constructed generator
     * @throws SemanticException When the syntax tree node contains errors.
     */
    private Generator buildGenerator(StGenerator expression) throws SemanticException {
        if (expression instanceof StNumber) {
            return Generator.get(((StNumber)expression).getValue());
        } else if (expression instanceof StColor) {
            return Generator.get(((StColor)expression).getValue());
        } else if (expression instanceof StVariable) {
            return scope.getConstant(expression.getCodePosition(), ((StVariable)expression).getName());
        } else if (expression instanceof StFunctionCall) {
            var fnCall = (StFunctionCall)expression;
            var args = new Generator[fnCall.getArgumentCount()];
            for (var i = 0; i < args.length; ++i) {
                args[i] = buildGenerator(fnCall.getArgument(i));
            }
            var f = scope.getFunction(fnCall.getCodePosition(), fnCall.getName());
            var g = f.call(fnCall.getCodePosition(), args);
            if (g.isNumber()) {
                return Generator.get(g.getDouble(0, 0));
            } else if (g.isColor()) {
                return Generator.get(g.getColor(0, 0));
            } else {
                g.setRandomSeed(fnCall.getSeed());
                return g;
            }
        } else {
            // should never happen - can only be caused by a bug in the parser
            throw new RuntimeException();
        }
    }

    /**
     * Converts a syntax tree node to a TypedArgument.
     * @param arg syntax tree node to convert
     * @return a TypedArgument instance equivalent to arg
     * @throws SemanticException When the syntax tree node contains errors.
     */
    private TypedArgument makeTypedArgument(StCommandArgument arg) throws SemanticException {
        if (arg instanceof StString) {
            return new TypedArgument(((StString)arg).getValue());
        } else {
            return new TypedArgument(buildGenerator((StGenerator)arg));
        }
    }

    /**
     * Checks whether the command has the correct number of arguments.
     * @param command command syntax tree node to check
     * @param count expected argument count
     * @throws SemanticException When there are too many or too few arguments.
     */
    private void checkArgumentCount(StCommand command, int count) throws SemanticException {
        if (command.getArgumentCount() != count) {
            if (count == 0) {
                throw error(command, command.getName() + " command takes no arguments");
            } else {
                throw error(command, command.getName() + " command expects " + count + " arguments");
            }
        }
    }

    /**
     * Checks types of all command arguments and converts them to TypedArgument.
     * @param command command node to use
     * @param argumentTypes expected argument types
     * @return array of converted arguments
     * @throws SemanticException When the command contains errors.
     */
    private TypedArgument[] getArguments(StCommand command, StType... argumentTypes) throws SemanticException {
        if (argumentTypes == null || argumentTypes.length == 0) {
            if (command.getArgumentCount() > 0) {
                throw error(command, command.getName() + " command takes no arguments");
            }
            return new TypedArgument[0];
        } else {
            if (command.getArgumentCount() != argumentTypes.length) {
                throw error(
                    command,
                    command.getName() + " command expects " + argumentTypes.length + " arguments"
                );
            }
            var ret = new TypedArgument[argumentTypes.length];
            for (int i = 0; i < argumentTypes.length; ++i) {
                var type = StType.get(command.getArgument(i));
                var success = false;
                ret[i] = makeTypedArgument(command.getArgument(i));
                if (argumentTypes[i].isAssignableFrom(type)) {
                    success = true;
                } else if (type == StType.GENERATOR && ret[i].getType() == argumentTypes[i]) {
                    success = true;
                }
                if (!success) {
                    throw error(
                        command.getArgument(i),
                        command.getName() + " command expects a " + argumentTypes[i]
                            + " as it's " + Utils.ordinal(i + 1) + " argument"
                    );
                }
            }
            return ret;
        }
    }

    /**
     * Executes a command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeCommand(StCommand command) throws SemanticException {
        switch (command.getName()) {
            case "Debugtex":
                executeDebugtexCommand(command);
                break;
            case "Debugtime":
                executeDebugtimeCommand(command);
                break;
            case "Filename":
                executeFilenameCommand(command);
                break;
            case "Import":
                executeImportCommand(command);
                break;
            case "Load":
                executeLoadCommand(command);
                break;
            case "Randomize":
                executeRandomizeCommand(command);
                break;
            case "Size":
                executeSizeCommand(command);
                break;
            case "Smoothing":
                executeSmoothingCommand(command);
                break;
            case "Texture":
                executeTextureCommand(command);
                break;
            case "Video":
                executeVideoCommand(command);
                break;
        }
    }

    /**
     * Executes "debug texture" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeDebugtexCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.NUMBER);
        var size = args[0].getNumber();
        if (size < 1.0 || size >= 8193) {
            throw error(command.getArgument(0), "Texture size has to be between 1 and 8192");
        }
        var textureSize = imageRenderer.getTextureSize();
        var outputFile = imageRenderer.getOutputFile();
        imageRenderer.setTextureSize((int)size);
        for (var variable: scope.listLocalVariables()) {
            imageRenderer.setOutputFile(outputFile + "_DEBUG_" + variable.getKey());
            imageRenderer.generateImageIgnoreDuplicates(
                command.getArgument(0).getCodePosition(),
                variable.getValue()
            );
        }
        imageRenderer.setTextureSize(textureSize);
        imageRenderer.setOutputFile(outputFile);
    }

    /**
     * Executes "debug time" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeDebugtimeCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.NUMBER);
        var time = args[0].getNumber();
        if (time < 0.0 || time >= 2.0) {
            throw error(command.getArgument(0), "Debugtime command takes a boolean as its only argument");
        }
        imageRenderer.setMeasureTime((int)time != 0);
    }

    /**
     * Executes "filename" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeFilenameCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.STRING);
        try {
            imageRenderer.setOutputFile(Paths.get(args[0].getString()).normalize().toString());
        } catch (InvalidPathException ex) {
            throw error(command.getArgument(0), "Output file name must be a valid path");
        }
    }

    /**
     * Executes "import" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeImportCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.STRING);
        scope.importPackage(command.getCodePosition(), args[0].getString());
    }

    /**
     * Executes "load" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeLoadCommand(StCommand command) throws SemanticException {
        checkArgumentCount(command, 2);
        String varName;
        String fileName;
        if (command.getArgument(0) instanceof StVariable) {
            varName = ((StVariable)command.getArgument(0)).getName();
        } else {
            throw error(
                command.getArgument(0),
                "Load command expects a variable as it's first argument"
            );
        }
        if (command.getArgument(1) instanceof StString) {
            fileName = Paths.get(((StString)command.getArgument(1)).getValue()).normalize().toString();
        } else {
            throw error(
                command.getArgument(1),
                "Load command expects a file name as it's second argument"
            );
        }
        BufferedImage img;
        try {
            img = ImageIO.read(directory.resolve(fileName).toFile());
        } catch (IOException e) {
            throw error(command.getArgument(1), "Could not load file '" + fileName + "'");
        }
        scope.createConstant(command.getCodePosition(), varName, Generator.get(img));
    }

    /**
     * Executes "randomize" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeRandomizeCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.NUMBER);
        imageRenderer.setRandomization((int)args[0].getNumber() != 0);
    }

    /**
     * Executes "size" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeSizeCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.NUMBER);
        var size = args[0].getNumber();
        if (size < 1.0 || size >= 8193) {
            throw error(command.getArgument(0), "Texture size has to be between 1 and 8192");
        }
        imageRenderer.setTextureSize((int)size);
    }

    /**
     * Executes "smoothing" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeSmoothingCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.NUMBER);
        var smoothing = args[0].getNumber();
        if (smoothing < 0.0 || smoothing >= 11.0) {
            throw error(command.getArgument(0), "Texture smoothing has to be between 0 and 10");
        }
        imageRenderer.setTextureSmoothing((int)smoothing);
    }

    /**
     * Executes "texture" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeTextureCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.GENERATOR);
        imageRenderer.generateImage(command.getArgument(0).getCodePosition(), args[0].getGenerator());
    }

    /**
     * Executes "video" command.
     * @param command command to execute
     * @throws SemanticException When the command contains errors.
     */
    private void executeVideoCommand(StCommand command) throws SemanticException {
        TypedArgument[] args = getArguments(command, StType.NUMBER, StType.GENERATOR);
        var frameCount = (int)args[0].getNumber();
        if (frameCount < 1) {
            throw error(command.getArgument(0), "Video has to have at least one frame");
        } else if (frameCount > 9_999) {
            throw error(command.getArgument(0), "Video can't have more than 9999 frames");
        }
        FrameLengthGenerator.setGlobalLength(1.0 / frameCount);
        for (var frame = 0; frame < frameCount; ++frame) {
            TimeGenerator.setGlobalTime((double)frame / frameCount);
            imageRenderer.generateFrame(command.getArgument(0).getCodePosition(), args[1].getGenerator(), frame);
        }
    }
}
