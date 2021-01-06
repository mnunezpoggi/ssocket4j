package com.mnunezpoggi.ssocket4j.server.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;

/**
 *
 * @author radxt
 */
public class SSLSimpleClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
          System.setProperty("javax.net.ssl.trustStore", 
            "testkeys");
          System.setProperty("javax.net.ssl.trustStorePassword", "thaadminrlz");
         SocketFactory sf = SSLSocketFactory.getDefault();
    Socket s = sf.createSocket("localhost", 5555);
    
    DataInputStream br = new DataInputStream(s.getInputStream());
    DataOutputStream pw = new DataOutputStream(s.getOutputStream());
    System.out.println("Sending: Who is Sylvia?");
    pw.writeUTF("Who is Sylvia?");
    pw.flush();
    System.out.println(br.readUTF());
    s.close();
    }
    
}