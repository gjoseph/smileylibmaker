package net.incongru.smileylibmaker.util.ftp;

public class FtpConfig {
    private String host;
    private String user;
    private String password;
    private boolean passive;
    private String remoteDir;

    public FtpConfig(String host, String user, String password, boolean passive, String remoteDir) {
        this.host = host;
        this.user = user;
        this.password = password;
        this.passive = passive;
        this.remoteDir = remoteDir;
    }

    public String getHost() {
        return host;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean getPassive() {
        return passive;
    }

    public String getRemoteDir() {
        return remoteDir;
    }
}
