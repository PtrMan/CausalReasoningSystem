package ptrman.causalReasoningSystem.functional.language.parseTree;

import java.util.List;

/**
 * Created by r0b3 on 09.12.2015.
 */
public class HasChildrenParseTreeElement extends ParseTreeElement {
    public final ParseTreeElement[] children;
    public final EnumType type;

    public enum EnumType {
        IDENTIFIER_OR_FUNCTION_OR_OPERATOR,
        STANDARDCALL_BRACES,

        PLUS,
        MINUS,
        MUL,
        VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, VARIABLE, CONSTANT_INTEGER, DIV
    }

    public HasChildrenParseTreeElement(EnumType type, ParseTreeElement[] children) {
        super();
        this.type = type;
        this.children = children;
    }
}
