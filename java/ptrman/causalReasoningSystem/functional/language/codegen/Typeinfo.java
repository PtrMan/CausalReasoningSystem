package ptrman.causalReasoningSystem.functional.language.codegen;

/**
 * typeinfo for a variable
 */
public class Typeinfo {
    public enum EnumType {
        BOOLEAN, INTEGER
    }

    public final EnumType type;

    public Typeinfo(EnumType type) {
        this.type = type;
    }

    public String getJavaTypeString() {
        if( type == EnumType.INTEGER ) {
            return "int";
        }

        throw new RuntimeException("Internal error!");
    }
}
