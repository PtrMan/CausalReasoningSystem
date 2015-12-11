package ptrman.causalReasoningSystem.functional.language.runtime;

/**
 * Created by r0b3 on 11.12.2015.
 */
class ResultAndControlflowPropagationInfo {
    int resultInteger;

    public static ResultAndControlflowPropagationInfo createResultForConstantInteger(final int value) {
        ResultAndControlflowPropagationInfo result = new ResultAndControlflowPropagationInfo();
        result.resultInteger = value;
        return result;
    }
}
