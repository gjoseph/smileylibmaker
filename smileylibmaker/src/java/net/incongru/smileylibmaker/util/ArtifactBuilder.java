package net.incongru.smileylibmaker.util;

import java.io.File;
import java.io.IOException;

/**
 * An interface for artifact builders. An artifact is a file
 * produced by a SmileyLibGenerator which is ready to be uploaded
 * or else distributed.
 */
public interface ArtifactBuilder {
    /**
     * Sets the File which will be the generated artifact.
     *
     * @param target
     */
    public void setArtifactFile(File target);

    /**
     * Retrieves the built file. Should throw an exception if
     * the build wasn't done yet.
     *
     * @return
     */
    public File getArtifactFile();

    /**
     * Adds one file to be included in the artifact.
     * @param f a File. Directories should be rejected.
     */
    void addSource(File f);
    
    void addDirectory(File dir, boolean recursive);

    public void build() throws IOException;
}
