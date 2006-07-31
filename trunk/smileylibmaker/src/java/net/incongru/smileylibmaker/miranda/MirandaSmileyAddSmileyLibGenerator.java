package net.incongru.smileylibmaker.miranda;

import net.incongru.smileylibmaker.AbstractSmileyLibGenerator;
import net.incongru.smileylibmaker.CommandLineXStreamConfigFactory;
import net.incongru.smileylibmaker.SmileyDescriptor;
import net.incongru.smileylibmaker.SmileyLibToolConfig;
import net.incongru.smileylibmaker.util.ProcessStreamReader;
import net.incongru.smileylibmaker.util.ZipBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.9 $
 */
public class MirandaSmileyAddSmileyLibGenerator extends AbstractSmileyLibGenerator {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private NConvertHandler nConvertHandler;
    private CommandLineXStreamConfigFactory configFactory;

    public MirandaSmileyAddSmileyLibGenerator(NConvertHandler nConvertHandler, CommandLineXStreamConfigFactory configFactory) {
        this.nConvertHandler = nConvertHandler;
        this.configFactory = configFactory;
    }

    public File generateSmileyLibrary(SmileyLibToolConfig config) throws IOException {
        String artifactsName = config.getLibraryName() + "-miranda-smileyadd-" + config.getVersion();
        File buildDir = cleanBuildDir(config.getBuildDir(), artifactsName);
        File icoBuildDir = cleanBuildDir(buildDir, "ico");
        File dllFile = new File(buildDir, artifactsName + ".dll");

        // .rc file
        File resourceDesc = new File(buildDir, config.getLibraryName() + ".rc");
        if (!resourceDesc.createNewFile()) {
            throw new IOException("Could not create file " + resourceDesc.getAbsolutePath());
        }
        Writer resourceDescWriter = new FileWriter(resourceDesc);
        StringBuffer resDescBuffer = new StringBuffer();

        // .msl file
        File mslFile = new File(buildDir, artifactsName + ".msl");
        if (!mslFile.createNewFile()) {
            throw new IOException("Could not create file " + mslFile.getAbsolutePath());
        }
        Writer mslWriter = new FileWriter(mslFile);
        StringBuffer mslBuffer = new StringBuffer();
        writeMslHeader(mslBuffer, config);//mslWriter);

        // convert all gifs to ico's and generate rc and msl files
        int resourceId = 1;
        //Iterator<SmileyDescriptor> it = config.getSmileys().iterator();
        Iterator it = config.getSmileys().iterator();
        while (it.hasNext()) {
            SmileyDescriptor smiley = (SmileyDescriptor) it.next();
            File f = smiley.getLocalCopy();
            File outFile = getOutputFile(icoBuildDir, f, ".ico");
            nConvertHandler.convertGifToIco(config, smiley, outFile);

            addIconToResourceDescriptor(resDescBuffer, outFile, resourceId);//resourceDescWriter
            addToMsl(mslBuffer, smiley, resourceId, dllFile.getName());//mslWriter
            resourceId++;
        }
        resourceDescWriter.write(resDescBuffer.toString()); //todo remove when switching to jdk1.5
        resourceDescWriter.close();

        mslWriter.write(mslBuffer.toString()); //todo remove when switching to jdk1.5
        mslWriter.close();

        // compile the dll
        String buildScriptPattern = configFactory.getOption(config, "Miranda build script path (use {0} for libraryname, {1} for result file, {2} for ico directory)");
        String command = MessageFormat.format(buildScriptPattern, new String[]{config.getLibraryName(), dllFile.getAbsolutePath(), icoBuildDir.getAbsolutePath()});
        try {
            //Todo: this is jdk1.5 style
            //            ProcessBuilder processBuilder = new ProcessBuilder(buildScriptPath,
            //                                                               config.getLibraryName(),
            //                                                               buildDir.getAbsolutePath(),
            //                                                               "D:\\projects\\smileylibmaker\\smileylibmaker\\src\\bin\\miranda");
            //            processBuilder.redirectErrorStream(true);
            //            Process dllBuildProcess = processBuilder.start();
            //BufferedInputStream buildOutput = new BufferedInputStream(dllBuildProcess.getInputStream());

            Process process = Runtime.getRuntime().exec(command);

            ProcessStreamReader reader = new ProcessStreamReader(process.getInputStream(), "   > ");
            ProcessStreamReader errorReader = new ProcessStreamReader(process.getErrorStream(), "   # ");

            reader.start();
            errorReader.start();
            int exitCode = process.waitFor();
            reader.join();
            errorReader.join();

            if (exitCode != 0) {
                throw new IOException("Exit code of the script was " + exitCode);
            } else if (!dllFile.exists()) {
                throw new IOException(dllFile + " could not be found.");
            }
        } catch (InterruptedException e) {
            throw new IOException("miranda build script's process was interrupted: (" + command + ")" + e.getMessage());
        }

        // build the zip
        ZipBuilder zipBuilder = new ZipBuilder(config.getBuildDir().getName() + File.separatorChar, null);
        zipBuilder.addSource(mslFile);
        zipBuilder.addSource(dllFile);
        File finalZipfile = new File(config.getBuildDir(), artifactsName + ".zip");
        zipBuilder.setArtifactFile(finalZipfile);
        zipBuilder.build();
        return zipBuilder.getArtifactFile();
    }

    //todo pass a Writer or Appendable when switch to jdk1.5
    private void writeMslHeader(StringBuffer writer, SmileyLibToolConfig config) throws IOException {
        // Name="hfrsmile"
        // Author="Drasche"
        // Date="22/05/2004"
        // Version="0.3.7.2"
        writer.append("Name=\"").append(config.getLibraryName()).append('"').append('\n');
        writer.append("Author=\"").append(config.getAuthor()).append('"').append('\n');
        writer.append("Date=\"").append(dateFormat.format(new Date())).append('"').append('\n');
        writer.append("Version=\"").append(config.getVersion()).append('"').append('\n');
        writer.append('\n');
    }

    //todo pass a Writer or Appendable when switch to jdk1.5
    private void addIconToResourceDescriptor(StringBuffer writer, File icoFile, int resourceId) throws IOException {
        // 1 ICON "icons\\Zoubi.ico"
        //String filename = outFile.getAbsolutePath().replace("\\", "\\\\").replace("/", "\\\\");//todo use the replace method once we switch to jdk1.5
        //String filename = outFile.getAbsolutePath().replaceAll("\\\\", "\\\\").replaceAll("/", "\\\\");//todo use the replace method once we switch to jdk1.5
        String filename = icoFile.getName();  // this path should be relative to the .rc file
        writer.append(String.valueOf(resourceId));
        writer.append(" ICON \"");
        writer.append(filename);
        writer.append("\"\n");
    }

    //todo pass a Writer or Appendable when switch to jdk1.5
    private void addToMsl(StringBuffer writer, SmileyDescriptor smiley, int resourceId, String dllFinalName) throws IOException {
        // Smiley = ".\hfrsmile.dll", -1, ":zoubi: [:zoubi]"
        writer.append("Smiley = \".\\").append(dllFinalName).append('"');
        writer.append(", -").append(String.valueOf(resourceId)).append(", ");
        writer.append('"');
        String[] keywords = smiley.getKeywords();
        if (keywords.length == 0) {
            String name = getFilenameWithoutExtension(smiley.getLocalCopy());
            keywords = new String[]{name};
        }
        for (int i = 0; i < keywords.length; i++) {
            if (i > 0) {
                writer.append(' ');
            }
            writer.append(':').append(keywords[i]).append(':');
            writer.append(' ');
            writer.append("[:").append(keywords[i]).append(']');
        }
        writer.append('"');
        writer.append('\n');
    }
}
