package ptrman.causalReasoningSystem.nlp;

import ptrman.causalReasoningSystem.InputGraph;

import java.util.*;

/**
 *
 */
public class Builder {
    public static class TokenWithIndex {
        String token;
        int index;

        public TokenWithIndex(String token, int index) {
            this.token = token;
            this.index = index;
        }
    }

    public static List<TokenWithIndex> fillGraphWithSentences(InputGraph graph, List<Sentence> sentences) {
        List<TokenWithIndex> uniqueTokens = calcUniqueTokens(sentences);
        final int lastTokenIndex = uniqueTokens.get(uniqueTokens.size()-1).index;
        int iterationSentenceIndex = lastTokenIndex+1;

        for( Sentence currentSentence : sentences ) {
            currentSentence.causalRootGraphIndex = iterationSentenceIndex;

            // connect from the words to the root
            // we do this in this direction because the sentences bind better to tighter bound words

            for( String iterationToken : currentSentence.tokens ) {
                int tokenIndex = getTokenIndex(uniqueTokens, iterationToken);
                graph.connections.add(new InputGraph.Connection(tokenIndex, currentSentence.causalRootGraphIndex));
            }

            iterationSentenceIndex++;
        }


        return uniqueTokens;
    }

    private static int getTokenIndex(final List<TokenWithIndex> tokenWithIndexes, final String token) {
        for( TokenWithIndex iterationToken : tokenWithIndexes ) {
            if( token.equals(iterationToken.token) ) {
                return iterationToken.index;
            }
        }

        throw new RuntimeException("Internal Error");
    }

    private static List<TokenWithIndex> calcUniqueTokens(List<Sentence> sentences) {
        Set<String> uniqueSet = new HashSet<>();

        for( Sentence iterationSentence : sentences ) {
            for( String iterationToken : iterationSentence.tokens ) {
                uniqueSet.add(iterationToken);
            }
        }

        String[] uniqueStringArray = new String[uniqueSet.size()];
        uniqueStringArray = uniqueSet.toArray(uniqueStringArray);

        List<TokenWithIndex> result = new ArrayList<>();
        int i = 0;

        for( String iterationToken : uniqueStringArray ) {
            result.add(new TokenWithIndex(iterationToken, i));
            i++;
        }

        return result;
    }
}
