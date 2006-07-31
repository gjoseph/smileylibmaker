package net.incongru.smileylibmaker.adium;

import net.incongru.smileylibmaker.AbstractTemplateBasedSmileyLibGenerator;
import net.incongru.smileylibmaker.SmileyLibToolConfig;
import net.incongru.smileylibmaker.util.ArtifactBuilder;
import net.incongru.smileylibmaker.util.TgzBuilder;

import java.io.File;

/**
 *
 */
public class AdiumSmileyLibGenerator extends AbstractTemplateBasedSmileyLibGenerator {

    public AdiumSmileyLibGenerator() {
    }

    protected ArtifactBuilder getArtifactBuilder(SmileyLibToolConfig config, File buildDir) {
        return new TgzBuilder(config.getBuildDir().getName() + File.separatorChar, buildDir);
    }

    protected String getFinalArtifactFileName(SmileyLibToolConfig config) {
        return config.getLibraryName() + "-adium-" + config.getVersion() + ".tgz";
    }

    protected String getDescriptorFileName(SmileyLibToolConfig config) {
        return "Emoticons.plist";
    }

    protected String getLibraryBuildDirName(SmileyLibToolConfig config) {
        return config.getLibraryName() + ".AdiumEmoticonset";
    }

}
