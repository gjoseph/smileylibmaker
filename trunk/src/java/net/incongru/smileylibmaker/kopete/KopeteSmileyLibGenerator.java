package net.incongru.smileylibmaker.kopete;

import net.incongru.smileylibmaker.AbstractTemplateBasedSmileyLibGenerator;
import net.incongru.smileylibmaker.SmileyDescriptor;
import net.incongru.smileylibmaker.SmileyLibToolConfig;
import net.incongru.smileylibmaker.util.ArtifactBuilder;
import net.incongru.smileylibmaker.util.PngConvertor;
import net.incongru.smileylibmaker.util.TgzBuilder;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class KopeteSmileyLibGenerator extends AbstractTemplateBasedSmileyLibGenerator {
    private PngConvertor pngConvertor;

    public KopeteSmileyLibGenerator(PngConvertor pngConvertor) {
        this.pngConvertor = pngConvertor;
    }

    protected void prepareImages(Collection smileys, File libBuildDir) throws IOException {
        Iterator it = smileys.iterator();
        while (it.hasNext()) {
            SmileyDescriptor smiley = (SmileyDescriptor) it.next();
            File f = smiley.getLocalCopy();
            String pngFileName = getFilenameWithoutExtension(f) + ".png";
            File copy = new File(libBuildDir, pngFileName);
            pngConvertor.convert(f, smiley.getFrame(), copy);
            //copyFile(f, copy);
        }
    }

    protected ArtifactBuilder getArtifactBuilder(SmileyLibToolConfig config, File buildDir) {
        return new TgzBuilder(config.getBuildDir().getName() + File.separatorChar, buildDir);
    }

    protected String getFinalArtifactFileName(SmileyLibToolConfig config) {
        return config.getLibraryName() + "-kopete-" + config.getVersion() + ".tgz";
    }

    protected String getDescriptorFileName(SmileyLibToolConfig config) {
        return "emoticons.xml";
    }

    protected String getLibraryBuildDirName(SmileyLibToolConfig config) {
        return config.getLibraryName() + "-kopete";
    }

}
