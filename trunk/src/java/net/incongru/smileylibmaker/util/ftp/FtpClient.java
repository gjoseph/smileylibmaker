package net.incongru.smileylibmaker.util.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPCommand;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.apache.commons.net.io.Util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class FtpClient extends FTPClient implements CopyStreamListener {
    private long toBeTransferred;

    public void bytesTransferred(CopyStreamEvent event) {
        bytesTransferred(event.getTotalBytesTransferred(), event.getBytesTransferred(), event.getStreamSize());
    }

    public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
        if (totalBytesTransferred < toBeTransferred) {
            System.out.print((totalBytesTransferred / 1024) + "k/" + (toBeTransferred / 1024) + "k\r");
        } else {
            System.out.println((toBeTransferred / 1024) + "k transfered.");
        }
    }

    public boolean storeFile(File f) throws IOException {
        toBeTransferred = f.length();
        FileInputStream fileInputStream = new FileInputStream(f);
        try {
            return storeFile(f.getName(), fileInputStream);
        } finally {
            fileInputStream.close();
        }
    }

    public boolean storeFile(String remote, InputStream local) throws IOException {
        int command = FTPCommand.STOR;
        OutputStream output;
        Socket socket;

        if ((socket = _openDataConnection_(command, remote)) == null)
            return false;

        output = new BufferedOutputStream(socket.getOutputStream(), Util.DEFAULT_COPY_BUFFER_SIZE);
        try {
            Util.copyStream(local, output, Util.DEFAULT_COPY_BUFFER_SIZE, CopyStreamEvent.UNKNOWN_STREAM_SIZE, this);
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException f) {
            }
            throw e;
        }
        output.close();
        socket.close();
        return completePendingCommand();
    }
}
