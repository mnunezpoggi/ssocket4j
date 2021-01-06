/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.client;
/**
 *
 * @author mauricio
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *  Class that matches a single connection on the server, responsible of
 *  the communication between client and server, it reads from an input stream
 *  an writes to an output stream. 
 * 
 * @author mauricio
 */
public class Client extends Thread {

    private Socket Socket_;
    //private DataInputStream InputStream_;
 //   private DataOutputStream OutputStream_;
    private PrintWriter OutputStream_;
    private int Port_;
    private String Hostname_;
    private int Id_;
    
    private ClientHandler Handler_;
    private String Host_;
    private Reader Reader_;

    public Client(String host, int port, ClientHandler Handler) {
        this.Host_ = host;
        this.Handler_ = Handler;
        this.Port_ = port;
        this.Reader_ = new Reader(Handler);
    }
    
    public boolean isConnected(){
         return this.Socket_ != null && !this.Socket_.isClosed();
    }
    
    public void connect() throws IOException{
        this.Socket_ = new Socket(Host_,Port_);
        this.Hostname_ = Socket_.getInetAddress().getHostAddress();
        
            OutputStream_ = new PrintWriter(
                                    new BufferedWriter(
                                            new OutputStreamWriter(
                                                    Socket_.getOutputStream())));
            this.Reader_.setSocket(Socket_);
            new Thread(Reader_).start();
        
    }
    
    public void connect(String host, int port) throws IOException{
        this.Host_ = host;
        this.Port_ = port;
        connect();
    }
 

    public void sendMessage(String message) {
        OutputStream_.println(message);
        OutputStream_.flush();
    }

    @Override
    public String toString() {
        String s = "";
        s = s + Hostname_ + " at port: " + Port_;
        return s;
    }

    }