package net.incongru.smileylibmaker.util;

import java.io.File;
import java.util.List;
import java.util.LinkedList;

/**
 * @author greg
 */
public abstract class AbstractArtifactBuilder implements ArtifactBuilder {
    protected String prefix;
    protected File dirSource;
    protected File target;
    protected List sources;
    protected boolean built;

    public AbstractArtifactBuilder(String prefix, File source) {
        if (source != null && (!source.isDirectory() || !source.exists())) {
            throw new IllegalArgumentException(source + " must be an existing directory.");
        }
        this.prefix = prefix;
        this.dirSource = source;
        this.sources = new LinkedList();
        this.built = false;
    }

    public void setArtifactFile(File target) {
        if (target.exists()) {
            throw new IllegalArgumentException(target + " must be a non-existing file.");
        }
        this.target = target;
    }

    public File getArtifactFile() {
        if (!built) {
            throw new IllegalStateException("Not built yet");
        }
        return target;
    }

    public void addSource(File f) {
        if (!f.isFile()|| !f.exists()) {
            throw new IllegalArgumentException(f + " must be an existing file.");
        }
        sources.add(f);
    }

    public void addDirectory(File dir, boolean recursive) {
        throw new IllegalStateException("Method addDirectory is not implemented yet.");
    }
}
