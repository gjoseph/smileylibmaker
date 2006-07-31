package net.incongru.smileylibmaker;

import net.incongru.smileylibmaker.util.ftp.FtpConfig;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.6 $
 */
public class SmileyLibToolConfig {
    private String libraryName;
    private String author;
    private String version;
    private File pathToLocalCopies;
    private File buildDir;
    private File newSmileysListFile;
    private FtpConfig ftpConfig;
    private Map options;
    //private Collection<SmileyDescriptor> smileys;
    private List smileys;

    SmileyLibToolConfig() {
        this.options = new HashMap();
        this.smileys = new LinkedList();
    }

    public File getPathToLocalCopies() {
        return pathToLocalCopies;
    }

    void setPathToLocalCopies(String pathToLocalCopiesStr) {
        this.pathToLocalCopies = checkDir(pathToLocalCopiesStr, "pathToLocalCopies");
    }

    public File getBuildDir() {
        return buildDir;
    }

    void setBuildDir(String buildDirStr) {
        this.buildDir = checkDir(buildDirStr, "buildDir");
    }

    public String getLibraryName() {
        return libraryName;
    }

    void setLibraryName(String libraryName) {
        this.libraryName = libraryName;
    }

    public String getAuthor() {
        return author;
    }

    void setAuthor(String author) {
        this.author = author;
    }

    public String getVersion() {
        return version;
    }

    void setVersion(String version) {
        this.version = version;
    }

    public FtpConfig getFtpConfig() {
        return ftpConfig;
    }

    void setFtpConfig(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

    String getOption(String key) {
        return (String) options.get(key);
    }

    void setOption(String key, String value) {
        options.put(key, value);
    }

    public Collection getSmileys() {
        return smileys;
    }

    public File getNewSmileysListFile() {
        return newSmileysListFile;
    }

    void setNewSmileysListFile(File newSmileysListFile) {
        this.newSmileysListFile = newSmileysListFile;
    }

    private File checkDir(String path, String argName) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        } else if (!f.isDirectory() || !f.canRead() || !f.canWrite()) {
            throw new IllegalArgumentException(argName + " must point to a valid and writable directory !");
        }
        return f;
    }
}
