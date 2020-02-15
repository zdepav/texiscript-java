package cz.zdepav.school.texiscript.script.runtime;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** @author Zdenek Pavlatka */
@FunctionalInterface
public interface Function {
    Generator call(CodePosition pos, Generator[] arguments) throws SemanticException;
}
