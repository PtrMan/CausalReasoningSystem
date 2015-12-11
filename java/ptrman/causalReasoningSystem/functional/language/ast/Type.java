package ptrman.causalReasoningSystem.functional.language.ast;

/**
 * Created by r0b3 on 09.12.2015.
 */
public class Type extends ptrman.causalReasoningSystem.functional.tree.Type {
    public enum EnumType {
        FUNCDAMENTAL_SEQUENCE,

        COND,
        FUNCTION_DEFINITION,
        FUNCTION_CALL,

        COMPARISION_LESS,
        COMPARISON_MESSEQUAL,
        COMPARISION_GREATER,
        COMPARISION_GREATEREQUAL,
        COMPARISION_EQUAL,
        COMPARISION_NOTEQUAL,

        /*
        OPERATION_SUBTRACTION,
        OPERATION_ADDITION,
        OPERATION_MULTIPLICATION,
        OPERATION_DIVISION,*/

        NIL,

        VARIABLE,

        CONSTANT_INTEGER
    }

    private final EnumType type;

    public Type(EnumType type) {
        this.type = type;
    }

    @Override
    public boolean isEqualWithType(ptrman.causalReasoningSystem.functional.tree.Type other) {
        return type == ((Type)other).type;
    }

    @Override
    public boolean isFundamentalType() {
        return type == EnumType.FUNCDAMENTAL_SEQUENCE;
    }

    @Override
    public EnumFundamentalType getFundamentalType() {
        if( type == EnumType.FUNCDAMENTAL_SEQUENCE ) {
            return EnumFundamentalType.SEQUENCE;
        }
        throw new RuntimeException("Internal Error");
    }
}
