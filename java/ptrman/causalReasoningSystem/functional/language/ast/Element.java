package ptrman.causalReasoningSystem.functional.language.ast;

import java.util.List;

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

    public static class DictElement {
        public final String key;
        public final Element valueElement;

        public DictElement(String keyIdentifier, Element valueElement) {
            this.key = keyIdentifier;
            this.valueElement = valueElement;
        }
    }

    public String identifier; // null if it is not an identififer
    public EnumCallType callType; // can be null if it is not a function call

    public int constantInteger; // only valid for a constant integer

    public List<DictElement> dictConstructorValues = null;

    public Element(Type type) {
        super();
        this.type = type;
    }

    @Override
    public boolean isEqual(ptrman.causalReasoningSystem.functional.tree.Element other) {
        return false;
    }
}
