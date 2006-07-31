package net.incongru.ico;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.2 $
 */
public class Gif2IcoTestAA extends IcoTestCaseA {
    public void testSimpleConversion() throws IOException {
        InputStream in = getClass().getResourceAsStream("/net/incongru/ico/test.gif");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Gif2Ico trans = new Gif2Ico(in, out);
        trans.convert();
        out.flush();

        InputStream expectedIn = getClass().getResourceAsStream("/net/incongru/ico/test.ico");
        assertStream(expectedIn, out.toByteArray());
    }

}
