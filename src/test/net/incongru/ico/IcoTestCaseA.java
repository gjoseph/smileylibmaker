package net.incongru.ico;

import junit.framework.TestCase;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.2 $
 */
public abstract class IcoTestCaseA extends TestCase {
    protected void assertStream(InputStream expectedIn, byte[] actual) throws IOException {
        byte[] temp = new byte[99999];
        int readLenght = expectedIn.read(temp);
        assertTrue("buffer was too small for expected data;", readLenght < temp.length);
        assertEquals("actual data did not have the right size;", readLenght, actual.length);
        byte[] expected = new byte[readLenght];
        System.arraycopy(temp, 0, expected, 0, readLenght);
        for (int i = 0; i < actual.length; i++) {
            assertEquals("byte " + i + " was different;", expected[i], actual[i]);
        }
    }
}
