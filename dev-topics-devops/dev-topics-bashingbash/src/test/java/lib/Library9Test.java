package lib;

import org.junit.Test;
import static org.junit.Assert.*;
import lib.Library9;

public class Library9Test {
    @Test public void testSomeLibraryMethod() {
        Library9 classUnderTest = new Library9();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }
}
