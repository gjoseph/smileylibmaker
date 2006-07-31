package net.incongru.smileylibmaker.util;

import net.incongru.tar.Tar;
import net.incongru.tar.TarCompressionMethod;

import java.io.File;
import java.io.IOException;

/**
 * @author greg
 */
public class TgzBuilder extends AbstractArtifactBuilder {
    public TgzBuilder(String prefix, File source) {
        super(prefix, source);
    }

    public void build() throws IOException {
        Tar tar = new Tar(target, dirSource, prefix, TarCompressionMethod.GZIP);
        tar.execute();
        built = true;
    }
    
    public void addSource(File f) {
        throw new IllegalStateException("Method addSource is not supported by TgzBuilder yet.");
    }
}
