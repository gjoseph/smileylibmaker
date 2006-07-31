package net.incongru.gif;

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
public class AnimatedGifFrameExtractor {
    public boolean extractFrame(File gif, int frameNumber, File out) throws IOException {
        ImageReader reader = getImageReader(gif);
        BufferedImage frame = reader.read(frameNumber);
        return ImageIO.write(frame, "gif", out);
    }

    public int countFrames(File gif) throws IOException {
        ImageReader reader = getImageReader(gif);
        return reader.getNumImages(true);
    }

    private ImageReader getImageReader(File gif) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(new FileInputStream(gif));
        //Iterator<ImageReader> readers = ImageIO.getImageReaders(imageInputStream);
        Iterator readers = ImageIO.getImageReaders(imageInputStream);
        if (!readers.hasNext()) {
            throw new IllegalArgumentException("no suitable reader found");
        }
        ImageReader reader = (ImageReader) readers.next();
        reader.setInput(imageInputStream);
        return reader;
    }
}
