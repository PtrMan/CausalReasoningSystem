package ptrman.causalReasoningSystem.functional.language.runtime;

/**
 * Used by the generated code to extract values or convert results from one type to another
 */
public class TypeConversation {
    public static int passThroughInteger(ResultAndControlflowPropagationInfo data) {
        return data.resultInteger;
    }

    /*
    public static int passThroughBoolean(ResultAndControlflowPropagationInfo data) {
        return data.resultBoolean;
    }
    */
}
