package net.incongru.smileylibmaker;

import net.incongru.smileylibmaker.util.ArtifactDeployer;

import java.io.File;
import java.io.IOException;

/**
 * Main entry point of the application.
 *
 * @author greg
 * @author $Author: greg $ (last edit)
 * @version $Revision: 1.6 $
 */
public class SmileyLibTool {
    private CommandLineXStreamConfigFactory configFactory;
    private SmileysUpdater smileysUpdater;
    private SmileyLibGenerator[] libGenerator;
    private ArtifactDeployer deployer;

    public SmileyLibTool(CommandLineXStreamConfigFactory configFactory, SmileysUpdater smileysUpdater, SmileyLibGenerator[] libGenerator, ArtifactDeployer deployer) {
        this.configFactory = configFactory;
        this.smileysUpdater = smileysUpdater;
        this.libGenerator = libGenerator;
        this.deployer = deployer;
    }

    public void start() throws IOException {
        System.out.println("Starting SmileyLibTool ...");

        SmileyLibToolConfig config = configFactory.load();

        smileysUpdater.update(config);

        File result[] = new File[libGenerator.length];

        for (int i = 0; i < libGenerator.length; i++) {
            String generatorName = libGenerator[i].getClass().getName();
            System.out.println("#" + (i + 1) + " Generating library with " + generatorName.substring(generatorName.lastIndexOf('.') + 1));
            result[i] = libGenerator[i].generateSmileyLibrary(config);
        }

        System.out.println();
        deployer.deploy(config, result);

        System.out.println();
        System.out.println("Finished all... Saving settings ...");
        configFactory.save(config);
    }

}
