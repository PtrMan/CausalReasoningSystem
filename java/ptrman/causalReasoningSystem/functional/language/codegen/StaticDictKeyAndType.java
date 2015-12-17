package ptrman.causalReasoningSystem.functional.language.codegen;

/**
 * Created by r0b3 on 13.12.2015.
 */
public class StaticDictKeyAndType {
    public final Typeinfo typeinfo;
    public final String key;

    public StaticDictKeyAndType(String key, Typeinfo typeinfo) {
        this.key = key;
        this.typeinfo = typeinfo;
    }
}
