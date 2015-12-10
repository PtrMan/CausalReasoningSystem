package ptrman.causalReasoningSystem.interactiveTests;

import ptrman.causalReasoningSystem.functional.language.ast.Element;
import ptrman.causalReasoningSystem.functional.language.convertParseTreeToAst.ConvertParseTreeToAst;
import ptrman.causalReasoningSystem.functional.language.parseTree.ParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parser.Parse;

/**
 * Created by r0b3 on 10.12.2015.
 */
public class InterpreterTest {
    public static void main(String[] args) {
        ParseTreeElement parseTree = Parse.parse("(a 5)");

        Element ast = ConvertParseTreeToAst.convert(parseTree);

        int x = 0;
    }

}
