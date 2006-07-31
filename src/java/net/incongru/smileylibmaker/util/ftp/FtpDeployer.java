package net.incongru.smileylibmaker.util.ftp;

import net.incongru.smileylibmaker.SmileyLibToolConfig;
import net.incongru.smileylibmaker.util.ArtifactDeployer;
import org.apache.commons.net.ftp.FTP;

import java.io.File;
import java.io.IOException;

/**
 * @author greg
 */
public class FtpDeployer implements ArtifactDeployer {

    public FtpDeployer() {
    }

    public void deploy(SmileyLibToolConfig config, File[] filesToDeploy) throws IOException {
        FtpClient ftp = new FtpClient();

        try {
            connect(ftp, config.getFtpConfig());
            for (int i = 0; i < filesToDeploy.length; i++) {
                if (filesToDeploy[i] != null) {
                    uploadFile(ftp, filesToDeploy[i]);
                } else {
                    System.out.println("filesToDeploy[" + i + "] is null, skipping...");
                }
            }
        } finally {
            disconnect(ftp);
        }
    }

    private void connect(FtpClient ftp, FtpConfig config) throws IOException {
        try {
            ftp.connect(config.getHost());
            ftp.login(config.getUser(), config.getPassword());
            if (config.getPassive()) {
                ftp.enterLocalPassiveMode();
            } else {
                ftp.enterLocalActiveMode();
            }
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.changeWorkingDirectory(config.getRemoteDir());
        } catch (IOException e) {
            if (ftp != null && ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException f) {
                    // do nothing
                }
            }
            throw e;
        }
    }

    private void disconnect(FtpClient ftp) {
        if (ftp != null && ftp.isConnected()) {
            try {
                ftp.logout();
                ftp.disconnect();
            } catch (IOException f) {
                // do nothing
            }
        }
    }

    private void uploadFile(FtpClient ftp, File f) throws IOException {
        System.out.println("Uploading " + f);
        ftp.storeFile(f);
    }
}
