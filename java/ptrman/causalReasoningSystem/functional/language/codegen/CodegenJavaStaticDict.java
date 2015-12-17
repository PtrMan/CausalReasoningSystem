package ptrman.causalReasoningSystem.functional.language.codegen;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Codegen for static dict classes for the java target
 */
public class CodegenJavaStaticDict {
    /**
     * \param argumentsMap names of arguments to values as string, can be incomplete, if it is incomplete default arguments will be passed to the constructor
     */
    public static void createJavaCodeForConstructorCall(final StaticDict dict, final Map<String, String> argumentsMap, StringBuilder codeBuilder) throws CannotCompileException {
        final List<String> argumentVariables = getCompleteArgumentsOfConstructorForArgumentsMap(dict, argumentsMap);
        final String completeArgumentsString = createArguments(argumentVariables);
        codeBuilder.append(String.format("%s(%s)", dict.javaClassName, completeArgumentsString));
    }

    public static void createJavaCodeForConstructorCallWithoutArguments(final StaticDict dict, StringBuilder codeBuilder) throws CannotCompileException {
        codeBuilder.append(String.format("%s()", dict.javaClassName));
    }

    private static List<String> getCompleteArgumentsOfConstructorForArgumentsMap(final StaticDict dict, final Map<String, String> argumentsMap) {
        List<String> resultOrderedArgumentsOfConstructor = new ArrayList<>();

        Set<Map.Entry<String, Typeinfo>> entries = getEntrySetOfConstructorForDict(dict);
        for( Map.Entry<String, Typeinfo> iterationEntry : entries ) {
            final String variableName = iterationEntry.getKey();

            String valueAsString;
            if( argumentsMap.containsKey(variableName) ) {
                valueAsString = argumentsMap.get(variableName);
            }
            else {
                valueAsString = iterationEntry.getValue().getJavaDefaultValue();
            }

            resultOrderedArgumentsOfConstructor.add(valueAsString);
        }

        return resultOrderedArgumentsOfConstructor;
    }

    private static Set<Map.Entry<String, Typeinfo>> getEntrySetOfConstructorForDict(StaticDict dict) {
        return dict.elementTypesByName.entrySet();
    }

    private static String createArguments(List<String> argumentVariables) {
        return String.join(", ", argumentVariables);
    }

    public static void fillClassDefinitionForStaticDict(CtClass classdefinition, StaticDict dict) throws CannotCompileException {
        fillFieldDefinitionForStaticDict(classdefinition, dict);
        fillMethodDefinitionForStaticDict(classdefinition, dict);
    }

    private static void fillMethodDefinitionForStaticDict(CtClass classdefinition, StaticDict dict) throws CannotCompileException {
        // define constructor
        StringBuilder functionCodeBuilder = new StringBuilder();

        functionCodeBuilder.append(String.format("public %s", dict.javaClassName));
        functionCodeBuilder.append("(");

        boolean constructorWithArguments = false;

        if( constructorWithArguments ) {
            getJavaStringOfParametersForConstructor(dict, functionCodeBuilder);
        }

        functionCodeBuilder.append(")");

        functionCodeBuilder.append(" {\n");

        for( Map.Entry<String, Typeinfo> iterationEntry : getEntrySetOfConstructorForDict(dict)) {
            functionCodeBuilder.append(String.format("   this.%s = %s;\n", iterationEntry.getKey(), iterationEntry.getKey()));
        }

        functionCodeBuilder.append("}\n");

        final String generatedCode = functionCodeBuilder.toString();

        // for debugging
        System.out.println(generatedCode);

        classdefinition.addMethod(CtMethod.make(generatedCode, classdefinition));
    }

    private static void getJavaStringOfParametersForConstructor(StaticDict dict, StringBuilder functionCodeBuilder) {
        Set<Map.Entry<String, Typeinfo>> entries = getEntrySetOfConstructorForDict(dict);

        int counter = 1;
        for( Map.Entry<String, Typeinfo> iterationEntry : entries ) {
            final boolean isLast = counter == entries.size();

            functionCodeBuilder.append(String.format("%s %s", iterationEntry.getValue().getJavaTypeString(), iterationEntry.getKey()));

            if( !isLast ) {
                functionCodeBuilder.append(", ");
            }

            counter++;
        }
    }

    private static void fillFieldDefinitionForStaticDict(CtClass classdefinition, StaticDict dict) throws CannotCompileException {
        for( String iterationVariablename : dict.elementTypesByName.keySet() ) {
            classdefinition.addField(new CtField(CtClass.booleanType, iterationVariablename, classdefinition));
        }
    }
}
