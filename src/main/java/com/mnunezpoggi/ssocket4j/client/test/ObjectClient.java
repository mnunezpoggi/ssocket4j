/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.client.test;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mnunezpoggi.ssocket4j.client.Client;
import com.mnunezpoggi.ssocket4j.client.ClientHandler;

/**
 *
 * @author mauricio
 */
public class ObjectClient {

    static Client c = null;
//    public static Socket s;
//    static ObjectOutputStream outputStream;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        c = new Client("localhost", 5555, new ClientHandler() {
            @Override
            public void onReceive(String o) {
                System.out.println("Recibido: " + o);
            }

            @Override
            public void onDisconnect() {
                System.out.println("desconectado");
                System.out.println(c.isConnected());
                while (!c.isConnected()) {
                    try {
                        System.out.println("Tratando de reconectarnos");
                        c.connect();
                        System.out.println(c.isConnected());
                        
                    } catch (IOException ex) {
                        System.out.println(ex);
                        System.out.println(c.isConnected());
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ObjectClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                        
                    
                }
                c.sendMessage("Conectados de nuevo");
            }
        });
        try {
            c.connect();
        } catch (IOException ex) {
            Logger.getLogger(ObjectClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        c.sendMessage("GET /81645359_zsd_62.jpg HTTP/1.1\r");
        c.sendMessage("Host: 192.168.0.14:8080\r");
        c.sendMessage("Connection: keep-alive\r");
        c.sendMessage("Upgrade-Insecure-Requests: 1\r");
        c.sendMessage("User-Agent: Mozilla/5.0 (X11; Linux i686) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36\r");
//c.sendMessage("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\r");
        c.sendMessage("Accept: application/base64\r");
        c.sendMessage("Accept-Encoding: utf-8\r");
        c.sendMessage("Accept-Language: en-US,en;q=0.8,es;q=0.6\r");
        c.sendMessage("\r");

    }
}
