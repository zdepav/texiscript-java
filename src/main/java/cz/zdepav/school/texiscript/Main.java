package cz.zdepav.school.texiscript;

import cz.zdepav.school.texiscript.script.interpreter.Interpreter;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.SyntaxException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

/** @author Zdenek Pavlatka */
public class Main {

    public static void main(String[] args) {
        /*for (var i = 0; i < 8; ++i) {
            testRandom(i);
        }
        testRandom2();*/
        for (var arg: args) {
            try {
                var file = Paths.get(arg).getFileName().toFile();
                System.out.println("Executing " + file);
                try (var input = new FileInputStream(arg)) {
                    var interpreter = new Interpreter(Paths.get(arg).getParent(), file.getName(), input);
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
//
    /*private static void testRandom(int predefs) {
        try (var f = new FileWriter("out" + predefs + ".dat")) {
            var distribution = new int[1000];
            for (var i = 0; i < 1024; ++i) {
                for (var j = 0; j < 1024; ++j) {
                    var rand = new SeededPositionedRandom(1024, i / 1024.0, j / 1024.0);
                    for (var k = 0; k < predefs; ++k) {
                        rand.r();
                    }
                    ++distribution[(int)(rand.r() * 1000)];
                }
            }
            for (var i = 0; i < 1000; ++i) {
                f.write(i + " " + distribution[i] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void testRandom2() {
        try (var f = new FileWriter("out8.dat")) {
            var distribution = new int[1000];
            var rand = new SeededPositionedRandom(1024, 3.1415, 9.2653);
            for (var i = 0; i < 1024; ++i) {
                for (var j = 0; j < 1024; ++j) {
                    ++distribution[(int)(rand.r() * 1000)];
                }
            }
            for (var i = 0; i < 1000; ++i) {
                f.write(i + " " + distribution[i] + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
}
