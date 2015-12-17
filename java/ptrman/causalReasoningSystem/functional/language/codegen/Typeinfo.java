package ptrman.causalReasoningSystem.functional.language.codegen;

/**
 * typeinfo for a variable
 */
public class Typeinfo {
    public boolean isEqual(Typeinfo other) {
        return type == other.type;
    }

    public String getJavaDefaultValue() {
        if( type == EnumType.INTEGER ) {
            return "0";
        }
        else if( type == EnumType.BOOLEAN ) {
            return "false";
        }

        throw new RuntimeException("Internal error!");
    }

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
        else if( type == EnumType.BOOLEAN ) {
            return "boolean";
        }

        throw new RuntimeException("Internal error!");
    }
}
