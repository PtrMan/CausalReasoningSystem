package ptrman.causalReasoningSystem.functional.language.parser;


import com.github.fge.grappa.parsers.BaseParser;
import com.github.fge.grappa.rules.Rule;
import com.github.fge.grappa.support.StringVar;
import com.github.fge.grappa.support.Var;
import ptrman.causalReasoningSystem.functional.language.parseTree.*;

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
                        variableOrConstantOrStandardCallBracesOrDictConstructor(), arguments.get().add(pop()), whiteSpaces(),
                        zeroOrMore(variableOrConstantOrStandardCallBracesOrDictConstructor(), arguments.get().add(pop()), whiteSpaces())
                ),
                ')',
                push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.STANDARDCALL_BRACES, convertElementAndListToArray(callIdentifier.get(), arguments.get()))));
    }

    Rule variableOrConstantOrStandardCallBracesOrDictConstructor() {
        return firstOf(
            sequence(standardCallBraces(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, new ParseTreeElement[]{pop()}))),
            sequence(variable(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, new ParseTreeElement[]{pop()}))),
            sequence(integer(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, new ParseTreeElement[]{pop()}))),
            sequence(dictConstructor(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES, new ParseTreeElement[]{pop()})))
        );
    }

    Rule dictConstructor() {
        Var<List<ParseTreeElement>> names = new Var<>(new ArrayList<>());
        Var<List<ParseTreeElement>> arguments = new Var<>(new ArrayList<>());

        return sequence(
            '{', whiteSpaces(),
            '\"', basicIdentifier(), names.get().add(pop()),'\"', whiteSpaces(), ':', whiteSpaces(), variableOrConstantOrStandardCallBracesOrDictConstructor(), arguments.get().add(pop()), whiteSpaces(),
            zeroOrMore(',', whiteSpaces(),  '\"', basicIdentifier(), names.get().add(pop()),'\"', whiteSpaces(), ':', whiteSpaces(), variableOrConstantOrStandardCallBracesOrDictConstructor(), arguments.get().add(pop()), whiteSpaces()),
            '}',
            push(new DictConstructorParseTreeElement(names.get(), arguments.get()))
        );
    }

    Rule identifierOrFunctionOrOperator() {
        return sequence(extendedIdentifier(), push(new HasChildrenParseTreeElement(HasChildrenParseTreeElement.EnumType.IDENTIFIER_OR_FUNCTION_OR_OPERATOR, new ParseTreeElement[]{pop()})));
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

    Rule extendedIdentifier() {
        StringVar var = new StringVar();

        return sequence(
                anyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ+-*/=<>"),
                var.append(match()),
                zeroOrMore(anyOf("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+-*/=<>")),
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



