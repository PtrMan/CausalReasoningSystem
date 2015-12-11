package ptrman.causalReasoningSystem.functional.language.codegen;

import javassist.CannotCompileException;
import javassist.CtMethod;
import ptrman.causalReasoningSystem.functional.language.ast.Element;
import ptrman.causalReasoningSystem.functional.language.ast.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javassist.CtClass;
import ptrman.causalReasoningSystem.functional.language.runtime.ResultAndControlflowPropagationInfo;

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
    private enum EnumMathematicalAccumulatorInitialValue {
        ONE,
        ZERO
    }

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

    // public for testing
    public InlinedResultCodeWithType generateBodyForwardPass(Element astElement, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        if( astElement.type.isEqualWithType(new Type(Type.EnumType.COND)) ) {
            return generateBodyForwardPassForCondition(astElement, typeinfoOfVariables);
        }
        else if( astElement.type.isEqualWithType(new Type(Type.EnumType.FUNCTION_CALL))) {
            if( astElement.identifier == null ) {
                if( astElement.callType == Element.EnumCallType.ADD ) {
                    return generateBodyForwardPassForMathematicalAccumulatorOperation("+", EnumMathematicalAccumulatorInitialValue.ZERO, astElement, typeinfoOfVariables);
                }
                else if( astElement.callType == Element.EnumCallType.SUB ) {
                    return generateBodyForwardPassForMathematicalAccumulatorOperation("-", EnumMathematicalAccumulatorInitialValue.ZERO, astElement, typeinfoOfVariables);
                }
                else if( astElement.callType == Element.EnumCallType.MUL ) {
                    return generateBodyForwardPassForMathematicalAccumulatorOperation("*", EnumMathematicalAccumulatorInitialValue.ONE, astElement, typeinfoOfVariables);
                }
                else if( astElement.callType == Element.EnumCallType.DIV ) {
                    return generateBodyForwardPassForMathematicalAccumulatorOperation("/", EnumMathematicalAccumulatorInitialValue.ONE, astElement, typeinfoOfVariables);
                }
                else {
                    throw new RuntimeException("Internal Error!");
                }
            }
            else {
                throw new RuntimeException("TODO< generate code for call of function >");
            }
        }
        else if( astElement.type.isEqualWithType(new Type(Type.EnumType.CONSTANT_INTEGER)) ) {
            return generateBodyForwardPassForIntegerConstant(astElement, typeinfoOfVariables);
        }
        else {
            throw new RuntimeException("Internal Error!");
        }
    }

    private InlinedResultCodeWithType generateBodyForwardPassForMathematicalAccumulatorOperation(String operationString, EnumMathematicalAccumulatorInitialValue accumulatorInitialValue, Element astElement, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        /*assert
                astElement.type.isEqualWithType(new Type(Type.EnumType.OPERATION_ADDITION)) ||
                        astElement.type.isEqualWithType(new Type(Type.EnumType.OPERATION_SUBTRACTION)) ||
                        astElement.type.isEqualWithType(new Type(Type.EnumType.OPERATION_MULTIPLICATION)) ||
                        astElement.type.isEqualWithType(new Type(Type.EnumType.OPERATION_DIVISION));*/

        StringBuilder resultCodeBuilder = new StringBuilder();

        final List<InlinedResultCodeWithType> inlinedResultCodeForArguments = inlineResultCodeForAllArguments(astElement, typeinfoOfVariables);
        final List<Typeinfo> typesOfArguments = getTypesOfInlinedResultCodeWithType(inlinedResultCodeForArguments);
        final Typeinfo resultType = getMergedTypes(typesOfArguments);

        resultCodeBuilder.append("// codegen: initial value\n");
        resultCodeBuilder.append(String.format("%s iterationResult = (%s)%s;\n", resultType.getJavaTypeString(), resultType.getJavaTypeString(), getJavaStringOfAccumulatorInitialValue(accumulatorInitialValue)));
        resultCodeBuilder.append("\n");
        resultCodeBuilder.append("\n");

        resultCodeBuilder.append("// codegen: accumulation\n");
        for( int argumentI = 0; argumentI < astElement.children.size(); argumentI++ ) {
            // the generated code accumulates over the accumulator
            // for the extraction of the value we call into a static method of the runtime which either extracts the typed value or converts the result value to the type
            resultCodeBuilder.append(String.format("iterationResult %s= %s.%s(%s);\n", operationString, RUNTIME_CLASS_FOR_TYPECONVERSATION, getJavaFunctionnameForConversationOrExtractionOfResultOfType(resultType, typesOfArguments.get(argumentI)), getJavaFunctioncallForFunction(inlinedResultCodeForArguments.get(argumentI).functionInfo)));
        }
        resultCodeBuilder.append("\n");
        resultCodeBuilder.append("\n");

        resultCodeBuilder.append(String.format("result = %s.%s(iterationResult);\n", RESULT_AND_PROPAGATION_TYPENAME, ResultAndControlflowPropagationInfo.codegenGetJavaFunctionnameForCreationOfType(resultType)));

        final String resultCode = resultCodeBuilder.toString();
        GeneratedFunctionInfo generatedFunctionInfo = emitInternalFunction(resultType, resultCode);

        return new InlinedResultCodeWithType(resultType, generatedFunctionInfo);
    }

    private String getJavaStringOfAccumulatorInitialValue(EnumMathematicalAccumulatorInitialValue accumulatorInitialValue) {
        if( accumulatorInitialValue == EnumMathematicalAccumulatorInitialValue.ONE ) {
            return "1";
        }
        else {
            return "0";
        }
    }

    private String getJavaFunctionnameForConversationOrExtractionOfResultOfType(Typeinfo resultType, Typeinfo sourceType) {
        if (resultType.type == Typeinfo.EnumType.INTEGER && sourceType.type == Typeinfo.EnumType.INTEGER) {
            return "passThroughInteger";
        }
        /*
        else if (resultType.type == Typeinfo.EnumType.BOOLEAN && sourceType.type == Typeinfo.EnumType.BOOLEAN) {
            return "passThroughBoolean";
        }
        */
        throw new RuntimeException("Internal error: Couldn't find out the Runtime method to pass or convert type to javatype!");
    }

    private List<Typeinfo> getTypesOfInlinedResultCodeWithType(List<InlinedResultCodeWithType> functionsWithTypes) {
        List<Typeinfo> result = new ArrayList<>();

        for( InlinedResultCodeWithType iterationGeneratedFunctionWithType : functionsWithTypes ) {
            result.add(iterationGeneratedFunctionWithType.returnType);
        }

        return result;
    }

    private List<InlinedResultCodeWithType> inlineResultCodeForAllArguments(Element astElement, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        List<InlinedResultCodeWithType> inlinedResultForArguments = new ArrayList<>();
        for( ptrman.causalReasoningSystem.functional.tree.Element iterationChildren : astElement.children ) {
            Element castedIterationChildren = (Element)iterationChildren;

            inlinedResultForArguments.add(generateBodyForwardPass(castedIterationChildren, typeinfoOfVariables));
        }

        return inlinedResultForArguments;
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

        StringBuilder resultCodeBuilder = new StringBuilder();

        resultCodeBuilder.append(String.format("boolean condition = %s;\n", getJavaFunctioncallForFunction(inlinedCheck.functionInfo)));
        resultCodeBuilder.append(              "if (condition) {\n");
        resultCodeBuilder.append(String.format("   result = %s;\n", getJavaFunctioncallForFunction(inlinedTruePath.functionInfo)));
        resultCodeBuilder.append(              "}\n");
        resultCodeBuilder.append(              "else {\n");
        resultCodeBuilder.append(String.format("   result = %s;\n", getJavaFunctioncallForFunction(inlinedFalsePath.functionInfo)));
        resultCodeBuilder.append(              "}\n");

        final String resultCode = resultCodeBuilder.toString();
        GeneratedFunctionInfo generatedFunctionInfo = emitInternalFunction(resultType, resultCode);

        return new InlinedResultCodeWithType(resultType, generatedFunctionInfo);
    }

    private InlinedResultCodeWithType generateBodyForwardPassForIntegerConstant(Element constant, Map<String, Typeinfo> typeinfoOfVariables) throws CannotCompileException {
        assert constant.type.isEqualWithType(new Type(Type.EnumType.CONSTANT_INTEGER));

        final Typeinfo resultType = new Typeinfo(Typeinfo.EnumType.INTEGER);

        String resultCode = String.format("result = %s.%s(%d);\n", RESULT_AND_PROPAGATION_TYPENAME, ResultAndControlflowPropagationInfo.codegenGetJavaFunctionnameForCreationOfType(resultType), constant.constantInteger);

        GeneratedFunctionInfo generatedFunctionInfo = emitInternalFunction(resultType, resultCode);

        return new InlinedResultCodeWithType(resultType, generatedFunctionInfo);
    }


    /*private static InlinedResultCodeWithType mergeInlinedResultCode(final InlinedResultCodeWithType a, final InlinedResultCodeWithType b) {

    }*/


    private String getJavaFunctioncallForFunction(GeneratedFunctionInfo functionInfo) {
        // TODO< parameters >
        return functionInfo.getInternalFunctionname() + "()";
    }


    /**
     * generates the final java code for a javafunction
     * A javafunction is generated for each Expression of the functional AST
     *
     */
    private GeneratedFunctionInfo emitInternalFunction(Typeinfo typeinfo, String code) throws CannotCompileException {
        GeneratedFunctionInfo generatedFunctionInfo = new GeneratedFunctionInfo(functionInternalNumberCounter);

        StringBuilder functionCodeBuilder = new StringBuilder();

        // for now public for debugging, later private
        functionCodeBuilder.append(String.format("public static %s %s() {\n", RESULT_AND_PROPAGATION_TYPENAME, generatedFunctionInfo.getInternalFunctionname()));

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

    private static Typeinfo getMergedTypes(final List<Typeinfo> types) {
        Typeinfo resultType = types.get(0);

        if( types.size() <= 1 ) {
            return resultType;
        }

        for( int i = 1; i < types.size(); i++ ) {
            resultType = getMergedTypes(resultType, types.get(i));
        }

        return resultType;
    }

    private static Typeinfo getMergedTypes(final Typeinfo a, final Typeinfo b) {
        if( a.type == b.type ) {
            return a;
        }

        // TODO< more info to show in error message! >
        throw new RuntimeException("Codegen Error: Can't merge types!");
    }

    private final static String RESULT_AND_PROPAGATION_TYPENAME = "ResultAndControlflowPropagationInfo";
    private final static String RUNTIME_CLASS_FOR_TYPECONVERSATION = "TypeConversation";

}
