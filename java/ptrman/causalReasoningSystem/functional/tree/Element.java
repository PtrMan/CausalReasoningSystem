package ptrman.causalReasoningSystem.functional.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Element of a expression of a functional language tree
 */
public abstract class Element {
    public Type type;
    public List<Element> children = new ArrayList<>();
    public abstract boolean isEqual(final Element other);
}
