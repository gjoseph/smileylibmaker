package net.incongru.smileylibmaker;

import java.io.File;
import java.io.IOException;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.1.1.1 $
 */
public interface SmileyLibGenerator {
    public File generateSmileyLibrary(SmileyLibToolConfig config) throws IOException;
}
