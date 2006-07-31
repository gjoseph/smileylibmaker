package net.incongru.smileylibmaker;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.2 $
 */
public class SmileyDescriptor implements Serializable {
    private URL originalLocation;
    private File localCopy;
    private Date lastRetrievalLastModifiedDate;
    private String[] keywords;
    private boolean completeKeywords; // if true then we won't add [: ] around keywords
    private int frame; //only valid for animated gifs... should we bother about designing good object oriented stuff? :p

    public SmileyDescriptor(URL originalLocation, String[] keywords) {
        this.originalLocation = originalLocation;
        this.keywords = keywords;
        this.frame = -1;
    }

    public URL getOriginalLocation() {
        return originalLocation;
    }

    public File getLocalCopy() {
        return localCopy;
    }


    public void setLocalCopy(File localCopy) {
        this.localCopy = localCopy;
    }

    /**
     * The last modified date attribute of the smiley's url when it was last retrieved.
     */
    public Date getLastRetrievalLastModifiedDate() {
        return lastRetrievalLastModifiedDate;
    }

    public void setLastRetrievalLastModifiedDate(Date lastRetrievalLastModifiedDate) {
        this.lastRetrievalLastModifiedDate = lastRetrievalLastModifiedDate;
    }

    public String[] getKeywords() {
        return keywords;
    }

    public boolean hasCompleteKeywords() {
        return completeKeywords;
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public String getFilenameWithoutExtension() {
        String name = localCopy.getName();
        int dotIdx = name.lastIndexOf('.');
        if (dotIdx < 0) {
            dotIdx = name.length();
        }
        return name.substring(0, dotIdx);
    }

    public String[] getKeywordsNoSpaces() {
        String[] s = new String[keywords.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = keywords[i].replaceAll(" ", "_");
        }
        return s;
    }
}
