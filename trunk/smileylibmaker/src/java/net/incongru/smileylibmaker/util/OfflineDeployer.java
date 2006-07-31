package net.incongru.smileylibmaker.util;

import net.incongru.smileylibmaker.SmileyLibToolConfig;

import java.io.File;
import java.io.IOException;

/**
 * @author greg
 */
public class OfflineDeployer implements ArtifactDeployer {
    public void deploy(SmileyLibToolConfig config, File[] filesToDeploy) throws IOException {
        for (int i = 0; i < filesToDeploy.length; i++) {
            System.out.println("Working in offline mode, not uploading " + filesToDeploy[i]);
        }
    }
}
