/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.client;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author radxt
 */
public class Reader implements Runnable {

    private final ClientHandler Handler_;
    private BufferedReader InputStream_;
    private Socket Socket_;

    public Reader(ClientHandler handler) {
        this.Handler_ = handler;
    }

    public void setSocket(Socket s) {
        Socket_ = s;
    }

    @Override
    public void run() {
        try {
            InputStream_ = new BufferedReader(
                    new InputStreamReader(Socket_.getInputStream()));
            while (true) {
                String o = InputStream_.readLine();
                if (o == null) {
                    clean();
                    break;
                }
                Handler_.onReceive(o);
            }
        } catch (EOFException e) {
            clean();     
        } catch (IOException ex) {
            clean();
        } finally {
            
        }
    }

    private void clean() {
        close();
        Handler_.onDisconnect();
        
    }

    private void close() {
        try {
            Socket_.close();
            Socket_ = null;
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
