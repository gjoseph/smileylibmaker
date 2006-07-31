package net.incongru.smileylibmaker.miranda;

import net.incongru.smileylibmaker.AbstractTemplateBasedSmileyLibGenerator;
import net.incongru.smileylibmaker.SmileyLibToolConfig;
import net.incongru.smileylibmaker.util.ArtifactBuilder;
import net.incongru.smileylibmaker.util.ZipBuilder;

import java.io.File;

/**
 *
 * @author greg
 * @author $Author: $ (last edit)
 * @version $Revision:  $
 */
public class MirandaIEViewSmileyLibGenerator extends AbstractTemplateBasedSmileyLibGenerator {
    protected String getTemplate() {
        return "template-ieview.vm";
    }

    protected ArtifactBuilder getArtifactBuilder(SmileyLibToolConfig config, File buildDir) {
        return new ZipBuilder(config.getBuildDir().getName() + File.separatorChar, buildDir);
    }

    protected String getFinalArtifactFileName(SmileyLibToolConfig config) {
        return filename(config) + ".zip";
    }

    protected String getDescriptorFileName(SmileyLibToolConfig config) {
        return filename(config) + ".asl";
    }

    private String filename(SmileyLibToolConfig config) {
        return getLibraryBuildDirName(config) + "-" + config.getVersion();
    }

    protected String getLibraryBuildDirName(SmileyLibToolConfig config) {
        return config.getLibraryName() + "-miranda-ieview";
    }
}
