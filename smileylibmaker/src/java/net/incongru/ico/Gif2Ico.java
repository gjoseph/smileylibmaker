package net.incongru.ico;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.1.1.1 $
 */
public class Gif2Ico {
    private InputStream in;
    private OutputStream out;

    public Gif2Ico(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void convert() throws IOException {
        BufferedImage bufferedImage = ImageIO.read(in);
        ImageIO.write(bufferedImage, "bmp", out);
    }
}
