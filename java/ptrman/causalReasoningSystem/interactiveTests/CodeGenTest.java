package ptrman.causalReasoningSystem.interactiveTests;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import ptrman.causalReasoningSystem.functional.language.ast.Element;
import ptrman.causalReasoningSystem.functional.language.codegen.CodegenJava;
import ptrman.causalReasoningSystem.functional.language.convertParseTreeToAst.ConvertParseTreeToAst;
import ptrman.causalReasoningSystem.functional.language.parseTree.ParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parser.Parse;

import java.util.HashMap;

/**
 * Created by r0b3 on 11.12.2015.
 */
public class CodeGenTest {
    public static void main(String[] args) {
        final ClassPool ctPool = new ClassPool(true);
        ctPool.importPackage("ptrman.causalReasoningSystem.functional.language.runtime");


        CtClass class0 = ctPool.makeClass("GeneratedCode");


        ParseTreeElement parseTree = Parse.parse("5");
        Element ast = ConvertParseTreeToAst.convert(parseTree);


        CodegenJava codegen = new CodegenJava(class0);
        try {
            codegen.generateBodyForwardPassForIntegerConstant(ast, new HashMap<>());
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
