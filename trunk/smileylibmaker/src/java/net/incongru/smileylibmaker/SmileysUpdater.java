package net.incongru.smileylibmaker;

import java.io.IOException;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.1.1.1 $
 */
public interface SmileysUpdater {
    void update(SmileyLibToolConfig config) throws IOException;
}
