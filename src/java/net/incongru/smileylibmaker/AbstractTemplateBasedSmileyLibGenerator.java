package net.incongru.smileylibmaker;

import net.incongru.smileylibmaker.util.ArtifactBuilder;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.VelocityException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

public abstract class AbstractTemplateBasedSmileyLibGenerator extends AbstractSmileyLibGenerator {

    public AbstractTemplateBasedSmileyLibGenerator() {
    }

    public File generateSmileyLibrary(SmileyLibToolConfig config) throws IOException {
        File libBuildDir = cleanBuildDir(config.getBuildDir(), getLibraryBuildDirName(config));

        // build descriptor
        VelocityContext ctx = new VelocityContext();
        ctx.put("config", config);

        InputStream templateInputStream = getResourceAsStreamRelativeToThisPackage(getTemplate());
        Reader template = new InputStreamReader(templateInputStream);
        File outFile = new File(libBuildDir, getDescriptorFileName(config));
        Writer outPlist = new FileWriter(outFile);

        try {
            Velocity.init();
            Velocity.evaluate(ctx, outPlist, this.getClass().getName(), template);
        } catch (VelocityException e) {
            throw new IOException(e.getClass().getName() + " : " + e.getMessage());
        } catch (Exception e) {
            throw new IOException(e.getClass().getName() + " : " + e.getMessage());
        } finally {
            outPlist.flush();
            outPlist.close();
            template.close();
        }

        prepareImages(config.getSmileys(), libBuildDir);

        // build artifact
        File artifact = new File(config.getBuildDir(), getFinalArtifactFileName(config));
        ArtifactBuilder artifactBuilder = getArtifactBuilder(config, libBuildDir);
        artifactBuilder.setArtifactFile(artifact);
        artifactBuilder.build();


        return artifact;
    }

    protected String getTemplate() {
        return "template.vm";
    }

    /**
     * By default, this simply copies the images over to the library's build dir.
     * Override this if a specific behaviour is required...
     */
    protected void prepareImages(Collection smileys, File libBuildDir) throws IOException {
        // just need to copy images
        Iterator it = smileys.iterator();
        while (it.hasNext()) {
            SmileyDescriptor smiley = (SmileyDescriptor) it.next();
            File f = smiley.getLocalCopy();
            File copy = new File(libBuildDir, f.getName());
            copyFile(f, copy);
        }
    }

    protected abstract ArtifactBuilder getArtifactBuilder(SmileyLibToolConfig config, File buildDir);

    protected abstract String getFinalArtifactFileName(SmileyLibToolConfig config);

    protected abstract String getDescriptorFileName(SmileyLibToolConfig config);

    protected abstract String getLibraryBuildDirName(SmileyLibToolConfig config);

    protected InputStream getResourceAsStreamRelativeToThisPackage(String resourceName) throws IOException {
        String className = getClass().getName();
        String packageName = className.substring(0, className.lastIndexOf('.'));
        String resourcePath = "/" + packageName.replace('.', '/') + "/" + resourceName;
        InputStream resource = getClass().getResourceAsStream(resourcePath);
        if (resource == null) {
            throw new IOException("Resource not found at path: " + resourcePath);
        }
        return resource;
    }
}
