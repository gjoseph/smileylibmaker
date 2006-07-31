package net.incongru.smileylibmaker.util;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;


/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.1.1.1 $
 */
public class PngConvertor {
    public boolean convert(File smiley, int frameNumber, File out) throws IOException {
        if (frameNumber < 0) {
            frameNumber = 0;
        }
        ImageReader reader = getImageReader(smiley);
        BufferedImage frame = reader.read(frameNumber);
        return ImageIO.write(frame, "png", out);
    }


    private ImageReader getImageReader(File gif) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(new FileInputStream(gif));
        Iterator readers = ImageIO.getImageReaders(imageInputStream);
        if (!readers.hasNext()) {
            throw new IllegalArgumentException("no suitable reader found");
        }
        ImageReader reader = (ImageReader) readers.next();
        reader.setInput(imageInputStream);
        return reader;
    }
}
