package net.incongru.smileylibmaker.util;

import net.incongru.smileylibmaker.SmileyLibToolConfig;

import java.io.File;
import java.io.IOException;

public interface ArtifactDeployer {
    void deploy(SmileyLibToolConfig config, File[] filesToDeploy) throws IOException;
}
