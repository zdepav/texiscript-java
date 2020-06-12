package cz.zdepav.school.texiscript;

import cz.zdepav.school.texiscript.script.interpreter.Interpreter;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.SyntaxException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        for (var arg: args) {
            try {
                var file = Paths.get(arg).getFileName().toFile();
                System.out.println("Executing " + file);
                try (var input = new FileInputStream(arg)) {
                    var directory = Paths.get(arg).getParent();
                    if (directory == null) {
                        directory = Paths.get(".");
                    }
                    var interpreter = new Interpreter(directory, file.getName(), input);
                    interpreter.execute();
                }
            } catch (SyntaxException ex) {
                System.err.println("Error on " + ex.getCodePosition() + ": " + ex.getMessage());
            } catch (SemanticException ex) {
                System.err.println("Error on " + ex.getCodePosition() + ": " + ex.getMessage());
            } catch (FileNotFoundException ex) {
                System.err.println("Could not find file '" + arg + '\'');
            } catch (IOException ex) {
                System.err.println("Could not read file '" + arg + '\'');
            } catch (RuntimeException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
