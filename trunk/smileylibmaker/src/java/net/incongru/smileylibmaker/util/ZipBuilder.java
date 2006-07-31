package net.incongru.smileylibmaker.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.Iterator;

/**
 * @author greg
 */
public class ZipBuilder extends AbstractArtifactBuilder {
    public ZipBuilder(String prefix, File source) {
        super(prefix, source);
    }

    public void build() throws IOException {
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(target));
        if (dirSource != null) {
            zipDirRecursive(dirSource, zos);
        }
        Iterator it = sources.iterator();
        while (it.hasNext()) {
            File file = (File) it.next();
            zipFile(file, zos);
        }
        zos.close();
        built = true;
    }

    private void zipDirRecursive(File sourceDir, ZipOutputStream zos) throws IOException {
        assert sourceDir.isDirectory() : sourceDir + " should be a directory";
        String[] dirList = sourceDir.list();

        for (int i = 0; i < dirList.length; i++) {
            File f = new File(sourceDir, dirList[i]);
            if (f.equals(target)) { // skip target. todo : maybe we should throw some exception so that idiot clients correct their code instead?
                System.out.println("Skipping self: can't include myself in myself. I'm not supple enough.");
                System.err.println("Skipping self: can't include myself in myself. I'm not supple enough.");
            } else if (f.isDirectory()) {
                zipDirRecursive(f, zos);
            } else {
                zipFile(f, zos);
            }
        }

    }

    private void zipFile(File f, ZipOutputStream zos) throws IOException {
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        FileInputStream in = new FileInputStream(f);
        String entryName = f.getPath();
        if (entryName.startsWith(prefix)) {
            entryName = entryName.substring(prefix.length());
        }
        ZipEntry anEntry = new ZipEntry(entryName);
        zos.putNextEntry(anEntry);
        while ((bytesIn = in.read(readBuffer)) != -1) {
            zos.write(readBuffer, 0, bytesIn);
        }
        in.close();
    }
}
