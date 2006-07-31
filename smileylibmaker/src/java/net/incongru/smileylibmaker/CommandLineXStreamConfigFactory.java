package net.incongru.smileylibmaker;

import com.thoughtworks.xstream.XStream;
import net.incongru.smileylibmaker.util.ftp.FtpConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.Collection;
import java.util.StringTokenizer;

public class CommandLineXStreamConfigFactory {
    private File configFile;

    public CommandLineXStreamConfigFactory(String configFilePath) {
        configFile = new File(configFilePath);
    }


    public String getOption(SmileyLibToolConfig config, String key) {
        try {
            String value = config.getOption(key);
            if (value == null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Please enter " + key + ":");
                value = in.readLine();
                config.setOption(key, value);
            }
            return value;
        } catch (IOException e) {
            e.printStackTrace();//TODO !!
            return null;
        }
    }

    public SmileyLibToolConfig load() throws IOException {
        if (configFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(configFile));
            XStream xStream = getSmileysXStream();
            SmileyLibToolConfig config = (SmileyLibToolConfig) xStream.fromXML(reader);
            update(config);
            return config;
        } else {
            SmileyLibToolConfig config = new SmileyLibToolConfig();
            setup(config);
            return config;
        }
    }

    private void update(SmileyLibToolConfig config) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter version number: [" + config.getVersion() + "]");
        String version = in.readLine();
        if (!("".equals(version))) {
            config.setVersion(version);
        }
        loadNewSmileysUrls(config.getNewSmileysListFile(), config.getSmileys());
    }

    private void setup(SmileyLibToolConfig config) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter file name with new smileys to download:");
        String newSmileysFilePath = in.readLine();
        System.out.println("Please enter directory name for local copies of images:");
        String pathToLocalCopies = in.readLine();
        System.out.println("Please enter directory for building:");
        String buildDir = in.readLine();
        System.out.println("Please enter library name:");
        String libraryName = in.readLine();
        System.out.println("Please enter author:");
        String author = in.readLine();
        System.out.println("Please enter version number:");
        String version = in.readLine();
        System.out.println("Please enter ftp hostname:");
        String ftphost = in.readLine();
        System.out.println("Please enter ftp username:");
        String ftpuser = in.readLine();
        System.out.println("Please enter ftp password:");
        String ftppass = in.readLine();
        System.out.println("Please enter ftp remote directory:");
        String ftpremotedir = in.readLine();
        System.out.println("Is ftp passive? (y/n)");
        boolean ftppassive = "y".equalsIgnoreCase(in.readLine());

        File newSmileysFile = new File(newSmileysFilePath);
        config.setNewSmileysListFile(newSmileysFile);
        loadNewSmileysUrls(newSmileysFile, config.getSmileys());
        config.setPathToLocalCopies(pathToLocalCopies);
        config.setBuildDir(buildDir);
        config.setLibraryName(libraryName);
        config.setAuthor(author);
        config.setVersion(version);
        config.setFtpConfig(new FtpConfig(ftphost, ftpuser, ftppass, ftppassive, ftpremotedir));
    }

    public void save(SmileyLibToolConfig config) throws IOException {
        if (configFile.exists()) {
            configFile.renameTo(new File(configFile + ".bak"));
        }

        Writer writer = new BufferedWriter(new FileWriter(configFile));
        XStream xStream = getSmileysXStream();
        xStream.toXML(config, writer);
    }

    private void loadNewSmileysUrls(File newSmileysFile, Collection smileys) throws IOException {
        if (newSmileysFile.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(newSmileysFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() > 1 && !line.startsWith("#")) {
                    StringTokenizer stok = new StringTokenizer(line, " ", false);
                    int tokenCount = stok.countTokens();
//                    if (tokenCount < 2) {
//                        throw new IllegalArgumentException("Each url must be followed by at least one keyword, separated by spaces.");
//                    }
                    String urlStr = stok.nextToken();
                    URL url = new URL(urlStr);
                    String[] keywords;
                    if (tokenCount > 1) {
                        keywords = new String[tokenCount - 1];
                        int i = 0;
                        while (stok.hasMoreTokens()) {
                            keywords[i] = stok.nextToken();
                            i++;
                        }
                    } else {
                        keywords = new String[]{getDefaultTokenName(url)};
                    }
                    smileys.add(new SmileyDescriptor(url, keywords));
                }
            }
        }
    }

    private String getDefaultTokenName(URL url) {
        String filename = url.getFile();
        int slashIdx = filename.lastIndexOf('/');
        if (slashIdx < 0) {
            slashIdx = 0;
        }
        int dotIdx = filename.lastIndexOf('.');
        if (dotIdx < 0) {
            dotIdx = filename.length();
        }
        return filename.substring(slashIdx + 1, dotIdx);
    }

    private XStream getSmileysXStream() {
        XStream xStream = new XStream();
        xStream.alias("SmileyLibToolConfig", SmileyLibToolConfig.class);
        xStream.alias("smiley", SmileyDescriptor.class);
        return xStream;
    }
}
