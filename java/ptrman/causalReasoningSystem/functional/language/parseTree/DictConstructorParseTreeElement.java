package ptrman.causalReasoningSystem.functional.language.parseTree;

import java.util.List;

/**
 * Created by r0b3 on 13.12.2015.
 */
public class DictConstructorParseTreeElement extends ParseTreeElement {
    public final List<ParseTreeElement> values;
    public final List<ParseTreeElement> names;

    public DictConstructorParseTreeElement(List<ParseTreeElement> names, List<ParseTreeElement> values) {
        this.names = names;
        this.values = values;
    }
}
