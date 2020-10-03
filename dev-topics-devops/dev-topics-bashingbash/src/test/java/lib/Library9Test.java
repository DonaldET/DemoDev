package lib;

import org.junit.Test;
import static org.junit.Assert.*;

public class Library9Test {
    @Test public void testSomeLibraryMethod() {
        lib.Library9 classUnderTest = new lib.Library9();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }
}
