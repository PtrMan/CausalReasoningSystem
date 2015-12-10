package ptrman.causalReasoningSystem.functional.language.parseTree;

import ptrman.causalReasoningSystem.functional.language.parseTree.ParseTreeElement;

/**
 * Created by r0b3 on 10.12.2015.
 */
public class BasicIntegerParseTreeElement extends ParseTreeElement {
    public final String constant;

    public BasicIntegerParseTreeElement(String constant) {
        super();
        this.constant = constant;
    }
}
