package ptrman.causalReasoningSystem.functional.language.parser;

import com.github.fge.grappa.Grappa;
import com.github.fge.grappa.run.ListeningParseRunner;
import com.github.fge.grappa.run.ParseRunner;
import com.github.fge.grappa.run.ParsingResult;
import ptrman.causalReasoningSystem.functional.language.parseTree.ParseTreeElement;

/**
 * Created by r0b3 on 10.12.2015.
 */
public class Parse {
    public static ParseTreeElement parse(String text) {
        Parser parser = Grappa.createParser(Parser.class);

        ParseRunner<ParseTreeElement> runner = new ListeningParseRunner<>(parser.variableOrConstantOrStandardCallBracesOrDictConstructor());
        ParsingResult<ParseTreeElement> parsingResult = runner.run(text);

        if (!parsingResult.isSuccess()) {
            throw new RuntimeException("Syntax Error!");
        }

        ParseTreeElement topElement = parsingResult.getTopStackValue();
        return topElement;
    }
}
