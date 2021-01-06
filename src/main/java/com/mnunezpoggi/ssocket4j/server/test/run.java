/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.server.test;

import com.mnunezpoggi.ssocket4j.server.Server;
import com.mnunezpoggi.ssocket4j.server.ServerThread;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mnunezpoggi.ssocket4j.server.SecureServer;
import com.mnunezpoggi.ssocket4j.server.exceptions.PortNotSetException;
import com.mnunezpoggi.ssocket4j.server.handlers.OnConnectHandler;
import com.mnunezpoggi.ssocket4j.server.handlers.OnReceiveHandler;

/**
 *
 * @author mauricio
 */
public class run {


    /**
     * @param args the command line arguments
     */
    static ArrayList<Integer> arduinos;

    public static void main(String[] args) {
        try {
            arduinos = new ArrayList();
            SecureServer s = new SecureServer();
            s.startSecureServer(5555, "testkeys", "thaadminrlz");
            s.addOnReceiveHandler(new onconnectimpl());
            

        } catch (IOException ex) {
            Logger.getLogger(run.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortNotSetException ex) {
            Logger.getLogger(run.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static class manejar implements OnReceiveHandler{
        Server se;
        public manejar(Server s){
            se = s;
        }
        @Override
        public void onReceive(String message, Integer key) {
            long sizeInKB = (Runtime.getRuntime().totalMemory() / 1024);
            long sizeInMB = sizeInKB / 1024;
            System.out.println("Memory: " + sizeInMB + " MB");
        }

        

    }

}
