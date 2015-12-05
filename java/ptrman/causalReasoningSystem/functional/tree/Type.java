package ptrman.causalReasoningSystem.functional.tree;

/**
 * Created by r0b3 on 04.12.2015.
 */
public abstract class Type {
    public enum EnumFundamentalType {
        SEQUENCE,
    }

    public abstract boolean isEqualWithType(final Type other);

    public abstract boolean isFundamentalType();
    public abstract EnumFundamentalType getFundamentalType();
}
