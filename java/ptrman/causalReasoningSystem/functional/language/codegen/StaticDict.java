package ptrman.causalReasoningSystem.functional.language.codegen;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains typeinfo about a static Dictionary
 */
public class StaticDict {
    public String javaClassName;

    public Map<String, Typeinfo> elementTypesByName = new HashMap<>();
}
