package ptrman.causalReasoningSystem.interactiveTests;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import ptrman.causalReasoningSystem.functional.language.ast.Element;
import ptrman.causalReasoningSystem.functional.language.codegen.CodegenJava;
import ptrman.causalReasoningSystem.functional.language.codegen.Typeinfo;
import ptrman.causalReasoningSystem.functional.language.convertParseTreeToAst.ConvertParseTreeToAst;
import ptrman.causalReasoningSystem.functional.language.parseTree.ParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parser.Parse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by r0b3 on 11.12.2015.
 */
public class CodeGenTest {
    public static void main(String[] args) {
        final ClassPool ctPool = new ClassPool(true);
        ctPool.importPackage("ptrman.causalReasoningSystem.functional.language.runtime");


        CtClass class0 = ctPool.makeClass("GeneratedCode");


        ParseTreeElement parseTree = Parse.parse("(cond booleanX 5 6)");
        Element ast = ConvertParseTreeToAst.convert(parseTree);


        CodegenJava codegen = new CodegenJava(ctPool, class0);

        Map<String, Typeinfo> variableTypes = new HashMap<>();
        variableTypes.put("booleanX", new Typeinfo(Typeinfo.EnumType.BOOLEAN));

        try {
            codegen.addVariableFieldsToVariableContainerForGeneratedFunctionalFunction(variableTypes);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        try {
            codegen.generateBodyForwardPass(ast, variableTypes);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }
}
