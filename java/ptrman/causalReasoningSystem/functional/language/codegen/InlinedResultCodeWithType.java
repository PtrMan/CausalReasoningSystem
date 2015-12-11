package ptrman.causalReasoningSystem.functional.language.codegen;

/**
 * Created by r0b3 on 10.12.2015.
 */
public class InlinedResultCodeWithType {
    ///public enum EnumCodetype {
        //DIRECTLINE, // code is/can be directly inlined
    //    STATICFUNCTION // a static function got allocated for the code, the body is "code", "allocatedFunctionname" is the functionname to be called
    //}

    //String functionCode;
    Typeinfo returnType;

    //EnumCodetype codetype;
    GeneratedFunctionInfo functionInfo;

    public InlinedResultCodeWithType(Typeinfo returnType, GeneratedFunctionInfo functionInfo) {
        this.returnType = returnType;
        this.functionInfo = functionInfo;
    }
}
