package ptrman.causalReasoningSystem.nlp;

import ptrman.causalReasoningSystem.InputGraph;

import java.util.List;
import java.util.Map;

/**
 *
 * Converts a sequences of any tokens with any repetitions into a representation where binders (depicted with []) point at the token before and after it
 *
 *          []
 *        /   \
 *     /        \
 *  V           V
 *  before     after
 */
public class BuilderForTokenstream {
    public static void build(InputGraph graph, Map<Integer, Integer> tokenToGraphIndices, int sequenceBeginningGraphIndex, List<Integer> sequence) {
        int currentBinderGraphIndex = sequenceBeginningGraphIndex;

        for( int sequenceIndex = 0; sequenceIndex < sequence.size()-1; sequenceIndex++ ) {
            final int iterationTokenIndexBefore = sequence.get(sequenceIndex);
            final int iterationTokenIndexAfter = sequence.get(sequenceIndex+1);

            final int graphIndexBefore = tokenToGraphIndices.get(iterationTokenIndexBefore);
            final int graphIndexAfter = tokenToGraphIndices.get(iterationTokenIndexAfter);

            graph.connections.add(new InputGraph.Connection(currentBinderGraphIndex, graphIndexBefore));
            graph.connections.add(new InputGraph.Connection(currentBinderGraphIndex, graphIndexAfter));

            currentBinderGraphIndex++;
        }
    }
}
