package ptrman.causalReasoningSystem.demos;

import ptrman.causalReasoningSystem.*;
import ptrman.causalReasoningSystem.nlp.BuilderForTokenstream;

import java.util.*;

/**
 * Created by r0b3 on 08.12.2015.
 */
public class UnsupervisedTokenstream {
    public static void main(String[] args) throws Exception {
        Map<String, Integer> tokenMap = new HashMap<>();

        List<Integer> tokens = new ArrayList<>();

        tokenize("", tokenMap, tokens);

        Map<Integer, Integer> tokensToGraphIndices = new HashMap<>();


        final int sequenceBeginningGraphIndex = tokenMap.size();

        createOneToOneTokenToGraphIndices(tokensToGraphIndices, tokenMap.size());

        InputGraph inputGraph = new InputGraph();

        BuilderForTokenstream.build(inputGraph, tokensToGraphIndices, sequenceBeginningGraphIndex, tokens);

        inputGraph.numberOfNodes = tokens.size() + (tokens.size() - 1);

        int x = 0;

        DecoratedCausalGraph causalGraph = ConvertInputGraphToCausalGraph.convert(inputGraph);

        //ArrayList<Integer> result = TrackbackGenerator.generate(new Random(), causalGraph);

        EnergyMinimizer.State state = new EnergyMinimizer.State();
        state.workingGraph = causalGraph;

        EnergyMinimizer.minimize(new Random(), 50000, state);


        int y = 0;
    }

    private static void createOneToOneTokenToGraphIndices(Map<Integer, Integer> target, int number) {
        for( int i = 0; i < number; i++ ) {
            target.put(i, i);
        }
    }

    private static void tokenize(final String text, Map<String, Integer> map, List<Integer> resultTokens) {
        // TODO< really tokenize >
        final List<String> stringTokens = Arrays.asList(new String[] { "i", "am", "a", "self", "!", "i", "am", "a", "machine", "!"});
        int tokenIndicesCounter = 0;

        for( String iterationToken : stringTokens ) {
            if( doesntContainsToken(map, iterationToken) ) {
                tokenIndicesCounter = addTokenToMap(map, tokenIndicesCounter, iterationToken);
            }

            resultTokens.add(map.get(iterationToken));
        }
    }

    private static int addTokenToMap(Map<String, Integer> map, int tokenIndicesCounter, String iterationToken) {
        map.put(iterationToken, tokenIndicesCounter);
        tokenIndicesCounter++;
        return tokenIndicesCounter;
    }

    private static boolean doesntContainsToken(Map<String, Integer> map, String iterationToken) {
        return !map.keySet().contains(iterationToken);
    }
}
