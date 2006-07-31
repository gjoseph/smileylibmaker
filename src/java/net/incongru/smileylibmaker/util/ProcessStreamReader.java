package net.incongru.smileylibmaker.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author rgagnon
 */
public class ProcessStreamReader extends Thread {
    //private InputStream in;
    private BufferedReader in;
    //private StringWriter sw;
    private String header;

    public ProcessStreamReader(InputStream is, String header) {
        this.in = new BufferedReader(new InputStreamReader(is));
        //this.sw = new StringWriter();
        this.header = header;
    }

    public void run() {
        try {
            /*int c;
            while ((c = in.read()) != -1){
                sw.write(c);
            }*/
            String line;
            while ((line = in.readLine()) != null){
                System.out.println(header + line);
            }
        } catch (IOException e) {
            ;
        }
    }

    public String getResult() {
        //return sw.toString();
        return null;
    }
}
