package net.incongru.smileylibmaker;

import net.incongru.gif.AnimatedGifFrameExtractor;
import net.incongru.smileylibmaker.adium.AdiumSmileyLibGenerator;
import net.incongru.smileylibmaker.gaim.GaimSmileyLibGenerator;
import net.incongru.smileylibmaker.kopete.KopeteSmileyLibGenerator;
import net.incongru.smileylibmaker.miranda.MirandaIEViewSmileyLibGenerator;
import net.incongru.smileylibmaker.miranda.MirandaSmileyAddSmileyLibGenerator;
import net.incongru.smileylibmaker.miranda.NConvertHandler;
import net.incongru.smileylibmaker.util.OfflineDeployer;
import net.incongru.smileylibmaker.util.PngConvertor;
import net.incongru.smileylibmaker.util.ftp.FtpDeployer;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.Parameter;
import org.picocontainer.defaults.ConstantParameter;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.picocontainer.defaults.PicoInvocationTargetInitializationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.8 $
 */
public class SmileyLibToolMain {
    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(BANNER);
        String version = "!unknown!";
        InputStream res = SmileyLibToolMain.class.getResourceAsStream("/version.properties");
        if (res != null) {
            Properties p = new Properties();
            p.load(res);
            version = p.getProperty("version");
        }
        System.out.println(" v" + version);
        System.out.println();

        Options opt = new Options();
        OptionGroup offline = new OptionGroup();
        offline.addOption(new Option("o", "offline", false, "Offline mode: does not download any new smiley nor upgrade existing ones."));
        offline.addOption(new Option("O", "real-offline", false, "Real offline more: like offline mode but does not upload generated libraries either."));
        opt.addOptionGroup(offline);
        Option helpOpt = new Option("h", "help", false, "Get some help");
        opt.addOption(helpOpt);
        CommandLineParser cliParser = new BasicParser();
        CommandLine cli = cliParser.parse(opt, args);
        if (cli.hasOption('h')) {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("SmileyLibMaker", opt);
            System.exit(0);
        }

        MutablePicoContainer pico = new DefaultPicoContainer();
        // util classes
        pico.registerComponentImplementation(AnimatedGifFrameExtractor.class);
        pico.registerComponentImplementation(NConvertHandler.class);
        //, NConvertHandler.class, new Parameter[]{
        //new ComponentParameter(AnimatedGifFrameExtractor.class),
        //new ConstantParameter("c:\\Program Files\\XnView\\nconvert.exe")});
        pico.registerComponentImplementation(PngConvertor.class, PngConvertor.class);

        // main classes
        pico.registerComponentImplementation(SmileyLibTool.class);
        pico.registerComponentImplementation(CommandLineXStreamConfigFactory.class, CommandLineXStreamConfigFactory.class, new Parameter[]{new ConstantParameter("config.xml")});
        if (cli.hasOption('O')) {
            pico.registerComponentImplementation(OfflineDeployer.class);
            pico.registerComponentImplementation(OfflineSmileysUpdater.class);
        } else {
            pico.registerComponentImplementation(FtpDeployer.class);
            if (cli.hasOption('o')) {
                pico.registerComponentImplementation(OfflineSmileysUpdater.class);
            } else {
                pico.registerComponentImplementation(LastModifiedAndNewSmileysUpdater.class);
            }
        }

        pico.registerComponentImplementation(MirandaSmileyAddSmileyLibGenerator.class);
        pico.registerComponentImplementation(MirandaIEViewSmileyLibGenerator.class);
        pico.registerComponentImplementation(AdiumSmileyLibGenerator.class);
        pico.registerComponentImplementation(GaimSmileyLibGenerator.class);
        pico.registerComponentImplementation(KopeteSmileyLibGenerator.class);

        // to be implemented
        //pico.registerComponentImplementation(PsiSmileyLibGenerator.class);
        //pico.registerComponentImplementation(TrillianSmileyLibGenerator.class);


        // get main component => start
        SmileyLibTool smileyLibTool = (SmileyLibTool) pico.getComponentInstance(SmileyLibTool.class);

        try {
            smileyLibTool.start();
        } catch (PicoInvocationTargetInitializationException e) {
            Throwable t = e;
            if (e.getCause() != null) {
                t = e.getCause();
            }
            String exname = t.getClass().getName();
            System.out.println();
            System.out.print(exname.substring(exname.lastIndexOf('.') + 1, exname.length()) + ": ");
            System.out.println(t.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String FLOWER_BANNER = "" +
        "   .-'''-. ,---.    ,---..-./`)   .---.       .-''-.     ____     __  .---.    .-./`)  _______   ,---.    ,---.   ____    .--.   .--.      .-''-.  .-------.\n" +
        "  / _     \\|    \\  /    |\\ .-.')  | ,_|     .'_ _   \\    \\   \\   /  / | ,_|    \\ .-.')\\  ____  \\ |    \\  /    | .'  __ `. |  | _/  /     .'_ _   \\ |  _ _   \\\n" +
        " (`' )/`--'|  ,  \\/  ,  |/ `-' \\,-./  )    / ( ` )   '    \\  _. /  ',-./  )    / `-' \\| |    \\ | |  ,  \\/  ,  |/   '  \\  \\| (`' ) /     / ( ` )   '| ( ' )  |\n" +
        "(_ o _).   |  |\\_   /|  | `-'`\"`\\  '_ '`) . (_ o _)  |     _( )_ .' \\  '_ '`)   `-'`\"`| |____/ / |  |\\_   /|  ||___|  /  ||(_ ()_)     . (_ o _)  ||(_ o _) /\n" +
        " (_,_). '. |  _( )_/ |  | .---.  > (_)  ) |  (_,_)___| ___(_ o _)'   > (_)  )   .---. |   _ _ '. |  _( )_/ |  |   _.-`   || (_,_)   __ |  (_,_)___|| (_,_).' __\n" +
        ".---.  \\  :| (_ o _) |  | |   | (  .  .-' '  \\   .---.|   |(_,_)'   (  .  .-'   |   | |  ( ' )  \\| (_ o _) |  |.'   _    ||  |\\ \\  |  |'  \\   .---.|  |\\ \\  |  |\n" +
        "\\    `-'  ||  (_,_)  |  | |   |  `-'`-'|___\\  `-'    /|   `-'  /     `-'`-'|___ |   | | (_{;}_) ||  (_,_)  |  ||  _( )_  ||  | \\ `'   / \\  `-'    /|  | \\ `'   /\n" +
        " \\       / |  |      |  | |   |   |        \\\\       /  \\      /       |        \\|   | |  (_,_)  /|  |      |  |\\ (_ o _) /|  |  \\    /   \\       / |  |  \\    /\n" +
        "  `-...-'  '--'      '--' '---'   `--------` `'-..-'    `-..-'        `--------`'---' /_______.' '--'      '--' '.(_,_).' `--'   `'-'     `'-..-'  ''-'   `'-'";

    public static final String BANNER =
    " ,---.            ,--.,--.                ,--.   ,--.,--.\n" +
        "'   .-' ,--,--,--.`--'|  | ,---. ,--. ,--.|  |   `--'|  |-.\n" +
        "`.  `-. |        |,--.|  || .-. : \\  '  / |  |   ,--.| .-. '\n" +
        ".-'    ||  |  |  ||  ||  |\\   --.  \\   '  |  '--.|  || `-' |\n" +
        "`-----' `--`--`--'`--'`--' `----'.-'  /   `-----'`--' `---'\n" +
        "    ,--.   ,--.        ,--.      `---'\n" +
        "    |   `.'   | ,--,--.|  |,-. ,---. ,--.--.\n" +
        "    |  |'.'|  |' ,-.  ||     /| .-. :|  .--'\n" +
        "    |  |   |  |\\ '-'  ||  \\  \\\\   --.|  |\n" +
        "    `--'   `--' `--`--'`--'`--'`----'`--'";
}
