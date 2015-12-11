package ptrman.causalReasoningSystem.functional.language.codegen;

/**
 * Created by r0b3 on 11.12.2015.
 */
public class GeneratedFunctionInfo {
    public final int internalNumber;

    public GeneratedFunctionInfo(int internalNumber) {
        this.internalNumber = internalNumber;
    }

    public String getInternalFunctionname() {
        return String.format("generatedFn%d", internalNumber);
    }
}
