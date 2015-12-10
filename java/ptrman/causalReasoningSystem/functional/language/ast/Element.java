package ptrman.causalReasoningSystem.functional.language.ast;

/**
 * Created by r0b3 on 10.12.2015.
 */
public class Element extends ptrman.causalReasoningSystem.functional.tree.Element {
    public enum EnumCallType {
        ADD,
        SUB,
        MUL,
        DIV
    }

    public String identifier; // null if it is not an identififer
    public EnumCallType callType; // can be null if it is not a function call

    public int constantInteger; // only valid for a constant integer

    public Element(Type type) {
        super();
        this.type = type;
    }

    @Override
    public boolean isEqual(ptrman.causalReasoningSystem.functional.tree.Element other) {
        return false;
    }
}
