package net.incongru.smileylibmaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.1.1.1 $
 */
public abstract class AbstractSmileyLibGenerator implements SmileyLibGenerator {

    public AbstractSmileyLibGenerator() {
    }

    protected File cleanBuildDir(File parentBuildDir, String buildSubdir) throws IOException {
        File buildDir = new File(parentBuildDir, buildSubdir);
        if (buildDir.exists()) {
            File[] files = buildDir.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (!files[i].delete()) {
                        throw new IOException("Could not remove file " + files[i].getAbsolutePath());
                    }
                }
            }
        } else {
            if (!buildDir.mkdirs()) {
                throw new IOException("Could not create directory " + buildDir.getAbsolutePath());
            }
        }
        return buildDir;
    }

    protected File getOutputFile(File buildDir, File originalFile, String extension) {
        String extensionLessFilename = getFilenameWithoutExtension(originalFile);
        String filename = extensionLessFilename + extension;
        File outFile = new File(buildDir, filename);
        return outFile;
    }

    protected String getFilenameWithoutExtension(File originalFile) {
        String originalFileName = originalFile.getName();
        int idx = originalFileName.lastIndexOf('.');
        return originalFileName.substring(0, idx);
    }

    protected void copyFile(File in, File out) throws IOException {
        FileChannel sourceChannel = new FileInputStream(in).getChannel();
        FileChannel destinationChannel = new FileOutputStream(out).getChannel();
        sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
        sourceChannel.close();
        destinationChannel.close();
    }
}
