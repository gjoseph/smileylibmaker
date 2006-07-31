package net.incongru.smileylibmaker;

import junit.framework.TestCase;

/**
 *
 * @author greg
 * @author $Author: $ (last edit)
 * @version $Revision:  $
 */
public class SmileyDescriptorTest extends TestCase {
    public void testNospaces() {
        SmileyDescriptor s = new SmileyDescriptor(null, new String[]{"yo yo", "pim pam pet"});
        String[] res = s.getKeywordsNoSpaces();
        assertEquals(2, res.length);
        assertEquals("yo_yo", res[0]);
        assertEquals("pim_pam_pet", res[1]);
    }
}
