package ptrman.causalReasoningSystem.functional;

import ptrman.causalReasoningSystem.InputGraph;
import ptrman.causalReasoningSystem.functional.tree.Element;

/**
 * translates a functional tree to a causal set
 */
public class Transformer {
    public static class Context {
        int index;
    }

    public static void transform(InputGraph graph, Context context, int entryGraphElement, Element entry) {
        final int thisIndex = entryGraphElement;
        graph.connections.add(new InputGraph.Connection(entryGraphElement, context.index));
        context.index++;

        for( Element iterationChildren : entry.children ) {
            transform(graph, context, thisIndex, iterationChildren);
        }
    }
}
