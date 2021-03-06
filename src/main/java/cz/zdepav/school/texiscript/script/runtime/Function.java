package cz.zdepav.school.texiscript.script.runtime;

import cz.zdepav.school.texiscript.generators.Generator;
import cz.zdepav.school.texiscript.script.interpreter.SemanticException;
import cz.zdepav.school.texiscript.script.parser.CodePosition;

/** Standard library function. */
@FunctionalInterface
public interface Function {

    /** Creates a generator from arguments. */
    Generator call(CodePosition pos, Generator[] arguments) throws SemanticException;
}
