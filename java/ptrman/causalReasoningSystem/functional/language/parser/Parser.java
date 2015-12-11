package ptrman.causalReasoningSystem.functional.language.parser;


import com.github.fge.grappa.parsers.BaseParser;
import com.github.fge.grappa.rules.Rule;
import com.github.fge.grappa.support.StringVar;
import com.github.fge.grappa.support.Var;
import ptrman.causalReasoningSystem.functional.language.parseTree.BasicIdentifierParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parseTree.BasicIntegerParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parseTree.HasChildrenParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parseTree.ParseTreeElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple functional grammar
 */
// TODO< spaces are not correct for all rules >
public class Parser extends BaseParser<ParseTreeElement> {
    Rule standardCallBraces() {
        Var<ParseTreeElement> callIdentifier = new Var<>();
        Var<List<ParseTreeElement>> arguments = new Var<>(new ArrayList<>());

        return sequence(
                '(',
                whiteSpaces(), identifierOrFunctionOrOperator(), callIdentifier.set(pop()), whiteSpaces(),
                optional(
                        variableOrConstantOrStandardCallBraces(), arguments.get().add(pop()), whiteSpaces(),
                        zeroOrMore(variableOrConstantOrStandardCallBraces(), arguments.get().add(pop()), whiteSpaces())
                ),
                ')',
                push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.STANDARDCALL_BRACES, convertElementAndListToArray(callIdentifier.get(), arguments.get()))));
    }

    Rule variableOrConstantOrStandardCallBraces() {
        return firstOf(
            sequence(standardCallBraces(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, new ParseTreeElement[]{pop()}))),
            sequence(variable(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, new ParseTreeElement[]{pop()}))),
            sequence(integer(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, new ParseTreeElement[]{pop()})))
        );
    }

    Rule identifierOrFunctionOrOperator() {
        // TODO< other cases >
        return firstOf(
                sequence(basicIdentifier(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.IDENTIFIER_OR_FUNCTION_OR_OPERATOR, new ParseTreeElement[]{pop()}))),
                sequence('+', push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.IDENTIFIER_OR_FUNCTION_OR_OPERATOR, new ParseTreeElement[]{new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.PLUS, new ParseTreeElement[]{})}))),
                sequence('-', push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.IDENTIFIER_OR_FUNCTION_OR_OPERATOR, new ParseTreeElement[]{new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.MINUS, new ParseTreeElement[]{})}))),
                sequence('*', push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.IDENTIFIER_OR_FUNCTION_OR_OPERATOR, new ParseTreeElement[]{new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.MUL, new ParseTreeElement[]{})}))),
                sequence('/', push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.IDENTIFIER_OR_FUNCTION_OR_OPERATOR, new ParseTreeElement[]{new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.DIV, new ParseTreeElement[]{})})))
        );
    }

    Rule variable() {
        return sequence(basicIdentifier(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE, new ParseTreeElement[]{pop()})));
    }

    Rule basicIdentifier() {
        StringVar var = new StringVar();

        return sequence(
                anyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
                var.append(match()),
                zeroOrMore(anyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789")),
                push(new BasicIdentifierParseTreeElement(var.get() + match()))
        );
    }

    Rule integer() {
        StringVar var = new StringVar();

        return sequence(
                anyOf("123456789"),
                var.append(match()),
                zeroOrMore(anyOf("0123456789")),
                push(new BasicIntegerParseTreeElement(var.get() + match()))
        );
    }


    // from http://stackoverflow.com/questions/33029437/matching-or-expression-using-grappa-java-peg-parser
    Rule whiteSpaces() {
        return join(zeroOrMore(wsp())).using(sequence(optional(cr()), lf())).min(0);
    }

    Rule whiteSpacesAtleastOne() {
        return join(oneOrMore(wsp())).using(sequence(optional(cr()), lf())).min(1);
    }

    // helpers

    protected static ParseTreeElement[] convertElementAndListToArray(ParseTreeElement element, List<ParseTreeElement> list) {
        ParseTreeElement[] result = new ParseTreeElement[1 + list.size()];

        result[0] = element;

        for( int i = 0; i < list.size(); i++ ) {
            result[i+1] = list.get(i);
        }

        return result;
    }
}



