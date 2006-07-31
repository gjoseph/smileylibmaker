package net.incongru.smileylibmaker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.3 $
 */
public class LastModifiedAndNewSmileysUpdater implements SmileysUpdater {

    public LastModifiedAndNewSmileysUpdater() {
    }

    public void update(SmileyLibToolConfig config) throws IOException {
        System.out.println("Updating all smileys... Downloading new or updated smileys... ");
        // todo : check if a "new" smiley is not already in the existing list
        int size = config.getSmileys().size();
        int i = 1;
        //Collection<SmileyDescriptor> newSmileys = config.getNewSmileys();
        Collection smileys = config.getSmileys();
        Iterator it = smileys.iterator();
        while (it.hasNext()) {
            System.out.print("    " + i + "/" + size + "\r");
            SmileyDescriptor smileyDescriptor = (SmileyDescriptor) it.next();
            //System.out.println("    " + smileyDescriptor.getOriginalLocation());
            URLConnection cnx = smileyDescriptor.getOriginalLocation().openConnection();
            Date urlLastModified = new Date(cnx.getLastModified());
            // if smiley needs to be updated
            if (smileyDescriptor.getLastRetrievalLastModifiedDate() == null || urlLastModified.after(smileyDescriptor.getLastRetrievalLastModifiedDate())) {
                File smileyFile = downloadSmiley(config.getPathToLocalCopies(), cnx);
                smileyDescriptor.setLocalCopy(smileyFile);
                smileyDescriptor.setLastRetrievalLastModifiedDate(new Date(cnx.getLastModified()));
            }
            i++;
        }
        System.out.println();
    }

    private File downloadSmiley(File pathToLocalCopies, URLConnection smileyUrlConnection) throws IOException {
        System.out.print("    " + smileyUrlConnection.getURL());
        String httpCode = smileyUrlConnection.getHeaderField(null);
        System.out.println(" -> " + httpCode);
        String contentType = smileyUrlConnection.getContentType();
        if (!(contentType.startsWith("image/"))) {
            throw new IOException(smileyUrlConnection.getURL() + " is not an image(" + contentType + ")");
        }
        File filename = new File(smileyUrlConnection.getURL().getFile());
        File outFile = new File(pathToLocalCopies, filename.getName());
        OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));
        InputStream in = new BufferedInputStream(smileyUrlConnection.getInputStream());
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        in.close();
        out.flush();
        out.close();
        return outFile;
    }
}
