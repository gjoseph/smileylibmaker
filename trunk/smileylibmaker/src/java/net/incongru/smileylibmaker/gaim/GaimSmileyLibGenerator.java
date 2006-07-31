package net.incongru.smileylibmaker.gaim;

import net.incongru.smileylibmaker.AbstractTemplateBasedSmileyLibGenerator;
import net.incongru.smileylibmaker.SmileyLibToolConfig;
import net.incongru.smileylibmaker.util.ArtifactBuilder;
import net.incongru.smileylibmaker.util.TgzBuilder;

import java.io.File;

public class GaimSmileyLibGenerator extends AbstractTemplateBasedSmileyLibGenerator {

    public GaimSmileyLibGenerator() {
    }

    protected ArtifactBuilder getArtifactBuilder(SmileyLibToolConfig config, File buildDir) {
        return new TgzBuilder(config.getBuildDir().getName() + File.separatorChar, buildDir);
    }

    protected String getFinalArtifactFileName(SmileyLibToolConfig config) {
        return config.getLibraryName() + "-gaim-" + config.getVersion() + ".tgz";
    }

    protected String getDescriptorFileName(SmileyLibToolConfig config) {
        return "theme";
    }

    protected String getLibraryBuildDirName(SmileyLibToolConfig config) {
        return config.getLibraryName() + "-gaim";
    }

}
