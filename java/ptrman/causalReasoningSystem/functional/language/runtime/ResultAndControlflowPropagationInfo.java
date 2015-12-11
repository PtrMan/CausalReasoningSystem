package ptrman.causalReasoningSystem.functional.language.runtime;

import ptrman.causalReasoningSystem.functional.language.codegen.Typeinfo;

/**
 * Used by the generated code to propagate results and controlflow information
 *
 */
public class ResultAndControlflowPropagationInfo {
    int resultInteger;
    boolean resultBoolean;

    // called from generated code
    public static ResultAndControlflowPropagationInfo createResultForInteger(final int value) {
        ResultAndControlflowPropagationInfo result = new ResultAndControlflowPropagationInfo();
        result.resultInteger = value;
        return result;
    }

    // called from generated code
    public static ResultAndControlflowPropagationInfo createResultForBoolean(final boolean value) {
        ResultAndControlflowPropagationInfo result = new ResultAndControlflowPropagationInfo();
        result.resultBoolean = value;
        return result;
    }


    // used by codegeneration
    public static String codegenGetJavaFunctionnameForCreationOfType(Typeinfo typeinfo) {
        if( typeinfo.type == Typeinfo.EnumType.INTEGER ) {
            return "createResultForInteger";
        }
        else if( typeinfo.type == Typeinfo.EnumType.BOOLEAN ) {
            return "createResultForBoolean";
        }

        throw new RuntimeException("Internal Error: No function for Static Runtime found to create value of type!");
    }

}
