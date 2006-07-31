package net.incongru.ico;

import javax.imageio.ImageIO;
import java.util.Arrays;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.2 $
 */
public class IcoWriterTestAA extends IcoTestCaseA {
    private static final String[] ico4bit = {"4bit/mlc.ico", "4bit/mmmfff.ico", "4bit/slackerbitch.ico", "4bit/smile.ico", "4bit/zebra33.ico"};
    private static final String[] ico8bit = {"8bit/3617buck.ico", "8bit/arg.ico", "8bit/atsuko.ico", "8bit/chacal_one333.ico", "8bit/chris94.ico", "8bit/hotshot.ico", "8bit/lorill.ico", "8bit/.ico"};
    private static final String[] ico24bit = {"24bit/aloy.ico", "24bit/pluto.ico"};

    protected void setUp() throws Exception {
        System.out.println("ImageIO.getReaderFormatNames() = " + Arrays.asList(ImageIO.getReaderFormatNames()));
        System.out.println("ImageIO.getWriterFormatNames() = " + Arrays.asList(ImageIO.getWriterFormatNames()));
    }

//    public void test4bit() throws IOException {
//        doTest(ico4bit);
//    }
//
//    public void test8bit() throws IOException {
//        doTest(ico8bit);
//    }
//
//    public void test24bit() throws IOException {
//        doTest(ico24bit);
//    }
//
//    private void doTest(String[] icons) throws IOException {
//        for (int i = 0; i < icons.length; i++) {
//            InputStream expected = getClass().getResourceAsStream(icons[i]);
//            BufferedImage img = ImageIO.read(expected);
//
//            Iterator writers = ImageIO.getImageWritersByFormatName("ico");
//            assertTrue(writers.hasNext());
//            ImageWriter writer = (ImageWriter) writers.next();
//            assertFalse(writers.hasNext());
//            assertTrue(writer instanceof IcoWriter);
//
//            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
//            ImageOutputStream out = new MemoryCacheImageOutputStream(byteOut);
//            writer.setOutput(out);
//            writer.write(img);
//        }
//    }

}
