package ptrman.causalReasoningSystem.functional.language.codegen;

import javassist.CannotCompileException;
import javassist.CtMethod;
import ptrman.causalReasoningSystem.functional.language.ast.Element;
import ptrman.causalReasoningSystem.functional.language.ast.Type;

import java.util.Map;

import javassist.CtClass;

/**
 *
 * Type used for propagating result and control flow information:
 *
 * abstract class ResultAndControlflowPropagationInfo {
 *    int resultInteger;
 * }
 */

/**
 * Created by r0b3 on 10.12.2015.
 */
// Inline : returns directly the code, without wrapping in an extrafunction
public class CodegenJava {
    private final CtClass ctClass;

    public CodegenJava(CtClass classTemplate) {
        this.ctClass = classTemplate;
    }

    public String generateFunctionByStaticTypes(Element astEntry, Map<String, Typeinfo> typeinfoOfVariables) {
        // TODO< uncomment, currently we are testing stuff >

        //if( !astEntry.returnType.isEqualWithType(new Type(Type.EnumType.FUNCTION_DEFINITION)) ) {
        //    throw new RuntimeException("Internal Error!");
        //}

        //return generateFunctionByStaticTypesInternalFunctionWasVerified(astEntry, typeinfoOfVariables).code;
        return null;
    }

    private InlinedResultCodeWithType generateFunctionByStaticTypesInternalFunctionWasVerified(Element astBody, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        return generateBodyForwardPass(astBody, typeinfoOfVariables);
    }

    private InlinedResultCodeWithType generateBodyForwardPass(Element astElement, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        if( astElement.type.isEqualWithType(new Type(Type.EnumType.COND)) ) {
            return generateBodyForwardPassForCondition(astElement, typeinfoOfVariables);
        }
        else {
            throw new RuntimeException("Internal Error!");
        }
    }

    private InlinedResultCodeWithType generateBodyForwardPassForCondition(Element condition, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        assert condition.type.isEqualWithType(new Type(Type.EnumType.COND));

        Element conditionCheck = (Element)condition.children.get(0);
        Element conditionTrue = (Element) condition.children.get(1);
        Element conditionFalse = (Element) condition.children.get(2);

        final InlinedResultCodeWithType inlinedCheck = generateBodyForwardPass(conditionCheck, typeinfoOfVariables);
        final InlinedResultCodeWithType inlinedTruePath = generateBodyForwardPass(conditionTrue, typeinfoOfVariables);
        final InlinedResultCodeWithType inlinedFalsePath = generateBodyForwardPass(conditionFalse, typeinfoOfVariables);

        final Typeinfo resultType = getMergedTypes(inlinedTruePath.returnType, inlinedFalsePath.returnType);

        if( inlinedCheck.returnType.type != Typeinfo.EnumType.BOOLEAN ) {
            throw new RuntimeException("Compilation error, condition has to be boolean!");
        }

        String resultCode = "";
        //functionCode += "" + resultType.getJavaTypeString() + " result;\n";
        resultCode += "boolean condition = " + getFunctionnameForInternalFunction(inlinedCheck.functionInfo) + "();" + "\n";
        resultCode += "if (condition) {\n";
        resultCode += "   result = " + getFunctionnameForInternalFunction(inlinedTruePath.functionInfo) + "();\n";
        resultCode += "}\n";
        resultCode += "else {\n";
        resultCode += "   result = " + getFunctionnameForInternalFunction(inlinedFalsePath.functionInfo) + "();\n";
        resultCode += "}\n";

        GeneratedFunctionInfo generatedFunctionInfo = emitInternalFunction(resultType, resultCode);

        return new InlinedResultCodeWithType(resultType, generatedFunctionInfo);
    }

    // for testing public
    public InlinedResultCodeWithType generateBodyForwardPassForIntegerConstant(Element constant, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        assert constant.type.isEqualWithType(new Type(Type.EnumType.CONSTANT_INTEGER));

        final Typeinfo resultType = new Typeinfo(Typeinfo.EnumType.INTEGER);

        String resultCode = String.format("result = %s.createResultForConstantInteger(%d);\n", RESULT_AND_PROPAGATION_TYPENAME, constant.constantInteger);

        GeneratedFunctionInfo generatedFunctionInfo = emitInternalFunction(resultType, resultCode);

        return new InlinedResultCodeWithType(resultType, generatedFunctionInfo);
    }

    /*private static InlinedResultCodeWithType mergeInlinedResultCode(final InlinedResultCodeWithType a, final InlinedResultCodeWithType b) {

    }*/

    private GeneratedFunctionInfo emitInternalFunction(Typeinfo typeinfo, String code) throws CannotCompileException {
        GeneratedFunctionInfo generatedFunctionInfo = new GeneratedFunctionInfo(functionInternalNumberCounter);

        StringBuilder functionCodeBuilder = new StringBuilder();

        // for now public for debugging, later private
        functionCodeBuilder.append(String.format("public static %s %s() {\n", RESULT_AND_PROPAGATION_TYPENAME, getFunctionnameForInternalFunction(generatedFunctionInfo)));

        functionCodeBuilder.append("// codegen: result allocation\n");
        functionCodeBuilder.append(RESULT_AND_PROPAGATION_TYPENAME + " result;\n");
        functionCodeBuilder.append("\n");
        functionCodeBuilder.append("\n");

        functionCodeBuilder.append("// codegen: generated code for function\n");
        functionCodeBuilder.append(code);
        functionCodeBuilder.append("\n");
        functionCodeBuilder.append("\n");

        functionCodeBuilder.append("// codegen: return result\n");
        functionCodeBuilder.append("return result;\n");


        functionCodeBuilder.append("}\n");

        functionCodeBuilder.append("\n");
        functionCodeBuilder.append("\n");

        final String generatedCode = functionCodeBuilder.toString();

        // for debugging
        System.out.println(generatedCode);

        ctClass.addMethod(CtMethod.make(generatedCode, ctClass));

        functionInternalNumberCounter++;

        return generatedFunctionInfo;
    }

    private int functionInternalNumberCounter = 0;

    private static Typeinfo getMergedTypes(final Typeinfo a, final Typeinfo b) {
        if( a.type == b.type ) {
            return a;
        }

        // TODO< more info to show in error message! >
        throw new RuntimeException("Codegen Error: Can't merge types!");
    }

    private static String getFunctionnameForInternalFunction(GeneratedFunctionInfo functionInfo) {
        return functionInfo.getInternalFunctionname();
    }

    private final static String RESULT_AND_PROPAGATION_TYPENAME = "ResultAndControlflowPropagationInfo";

}
