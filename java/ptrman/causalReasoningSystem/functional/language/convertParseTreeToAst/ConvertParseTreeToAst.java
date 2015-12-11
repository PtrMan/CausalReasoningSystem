package ptrman.causalReasoningSystem.functional.language.convertParseTreeToAst;

import ptrman.causalReasoningSystem.functional.language.ast.Type;
import ptrman.causalReasoningSystem.functional.language.parseTree.BasicIdentifierParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parseTree.BasicIntegerParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parseTree.HasChildrenParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.parseTree.ParseTreeElement;
import ptrman.causalReasoningSystem.functional.language.ast.Element;

/**
 * Created by r0b3 on 10.12.2015.
 */
public class ConvertParseTreeToAst {
    public static Element convert(ParseTreeElement root) {
        if( root instanceof BasicIdentifierParseTreeElement ) {
            BasicIdentifierParseTreeElement convertedRoot = (BasicIdentifierParseTreeElement)root;

            Element result = new Element(new Type(Type.EnumType.VARIABLE));
            result.identifier = convertedRoot.identifier;
            return result;
        }
        else if( root instanceof BasicIntegerParseTreeElement ) {
            BasicIntegerParseTreeElement convertedRoot = (BasicIntegerParseTreeElement) root;

            Element result = new Element(new Type(Type.EnumType.CONSTANT_INTEGER));
            result.constantInteger = Integer.parseInt(convertedRoot.constant);
            return result;
        }
        else if( root instanceof HasChildrenParseTreeElement ) {
            HasChildrenParseTreeElement convertedRoot = (HasChildrenParseTreeElement)root;

            if( convertedRoot.type == HasChildrenParseTreeElement.EnumType.STANDARDCALL_BRACES ) {
                /**
                 * the result is a FUNCTION_CALL where the identifier is set to the function name or the calltype is set to the calltype
                 */
                // TODO ULTRALOW< maybe the component pattern improves this but its overkill because it adds unneeded complexity >

                Element result = new Element(new Type(Type.EnumType.FUNCTION_CALL));

                HasChildrenParseTreeElement calledFunctionIdentifierOrOperator = ((HasChildrenParseTreeElement)convertedRoot.children[0]);
                if( calledFunctionIdentifierOrOperator.children[0] instanceof BasicIdentifierParseTreeElement) {
                    BasicIdentifierParseTreeElement calleeNameTreeElement = (BasicIdentifierParseTreeElement)calledFunctionIdentifierOrOperator.children[0];
                    String calleeName = calleeNameTreeElement.identifier;
                    result.identifier = calleeName;
                }
                else {
                    HasChildrenParseTreeElement operator = (HasChildrenParseTreeElement)calledFunctionIdentifierOrOperator.children[0];

                    if( operator.type == HasChildrenParseTreeElement.EnumType.PLUS ) {
                        result.callType = Element.EnumCallType.ADD;
                    }
                    else if( operator.type == HasChildrenParseTreeElement.EnumType.MINUS ) {
                        result.callType = Element.EnumCallType.SUB;
                    }
                    else if( operator.type == HasChildrenParseTreeElement.EnumType.MUL ) {
                        result.callType = Element.EnumCallType.MUL;
                    }
                    else if( operator.type == HasChildrenParseTreeElement.EnumType.DIV ) {
                        result.callType = Element.EnumCallType.DIV;
                    }
                    else {
                        throw new RuntimeException("Internal error!");
                    }
                }

                // translate all operands
                for( int childrenI = 1; childrenI < convertedRoot.children.length; childrenI++ ) {
                    result.children.add(convert(convertedRoot.children[childrenI]));
                }

                return result;
            }
            else if( convertedRoot.type == HasChildrenParseTreeElement.EnumType.CONSTANT_INTEGER ) {
                BasicIntegerParseTreeElement constantParseTreeElement = ((BasicIntegerParseTreeElement)convertedRoot.children[0]);

                Element result = new Element(new Type(Type.EnumType.CONSTANT_INTEGER));
                result.constantInteger = Integer.parseInt(constantParseTreeElement.constant);
                return result;
            }
            else if( convertedRoot.type == HasChildrenParseTreeElement.EnumType.VARIABLE_OR_CONSTANT_OR_STANDARD_CALL_BRACES ) {
                // simplfy it
                return convert(convertedRoot.children[0]);
            }
            else if( convertedRoot.type == HasChildrenParseTreeElement.EnumType.VARIABLE ) {
                BasicIdentifierParseTreeElement identiferParseTreeElement = (BasicIdentifierParseTreeElement) convertedRoot.children[0];

                Element result = new Element(new Type(Type.EnumType.VARIABLE));
                result.identifier = identiferParseTreeElement.identifier;
                return result;
            }
            else {
                throw new RuntimeException("Internal Error!");
            }
        }
        else {
            throw new RuntimeException("Internal error!");
        }
    }
}
