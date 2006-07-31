package net.incongru.smileylibmaker.miranda;

import net.incongru.gif.AnimatedGifFrameExtractor;
import net.incongru.smileylibmaker.CommandLineXStreamConfigFactory;
import net.incongru.smileylibmaker.SmileyDescriptor;
import net.incongru.smileylibmaker.SmileyLibToolConfig;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.5 $
 */
public class NConvertHandler {
    private AnimatedGifFrameExtractor animatedGifFrameExtractor;
    private CommandLineXStreamConfigFactory configFactory;

    public NConvertHandler(AnimatedGifFrameExtractor animatedGifFrameExtractor, CommandLineXStreamConfigFactory configFactory) {
        this.animatedGifFrameExtractor = animatedGifFrameExtractor;
        this.configFactory = configFactory;
    }

    public boolean convertGifToIco(SmileyLibToolConfig config, SmileyDescriptor smileyDescriptor, File outFile) throws IOException {
        String nConvertExePattern = configFactory.getOption(config, "nConvert command (use {0} for source file, {1} for target file)");
        File original = smileyDescriptor.getLocalCopy();
        int countFrames = animatedGifFrameExtractor.countFrames(original);
        File in;
        if (smileyDescriptor.getFrame() < 0 && countFrames > 1) {
            // this is an animated gif and we don't know what frame the user wants, callback !
            // todo throw new IllegalStateException("we must implement a callback to get the frame number for animated gifs");
            in = original;
        } else if (countFrames > 1) {
            // this is an animated gif and we know what frame the user wants
            in = File.createTempFile("smileylibmaker", ".temp.gif");
            animatedGifFrameExtractor.extractFrame(original, smileyDescriptor.getFrame(), in);
        } else {
            in = original;
        }

//        String command = nConvertExeFullPath + " -out ico -o " + outFile.getAbsolutePath() + " " + in.getAbsolutePath();
        String command = MessageFormat.format(nConvertExePattern, new Object[] {in.getAbsolutePath(), outFile.getAbsolutePath()});
        Process process = Runtime.getRuntime().exec(command);
        try {
            int exitValue = process.waitFor();
            return exitValue == 0;
        } catch (InterruptedException e) {
            throw new IOException("NConvert process was interrupted: " + e.getMessage());
        }
    }
}
