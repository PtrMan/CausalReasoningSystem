import org.junit.Test;
import ptrman.causalReasoningSystem.functional.language.parser.Parse;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestParse {
    @Test
    public void test() {
        // zero parameter
        Parse.parse("(a)");

        Parse.parse("(+ )");

        // two parameters
        Parse.parse("(call0 a b)");

        // one parameter
        Parse.parse("(call0 a)");



        // call in call
        Parse.parse("(call0 (a))");

        // integer constants
        Parse.parse("(+ 5 6)");
    }
}