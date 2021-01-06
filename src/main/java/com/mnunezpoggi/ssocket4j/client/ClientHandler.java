/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.client;

import java.net.Socket;

/**
 *
 * @author mauricio
 */
public interface ClientHandler {

    /**
     * @param args the command line arguments
     */
    public void onReceive(String o);
    
    public void onDisconnect();
    
}
