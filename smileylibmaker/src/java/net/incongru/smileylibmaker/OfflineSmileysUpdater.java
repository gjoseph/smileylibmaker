package net.incongru.smileylibmaker;

import java.io.IOException;

public class OfflineSmileysUpdater implements SmileysUpdater {
    public void update(SmileyLibToolConfig config) throws IOException {
        System.out.println("Working offline, not updating :p");
    }
}
