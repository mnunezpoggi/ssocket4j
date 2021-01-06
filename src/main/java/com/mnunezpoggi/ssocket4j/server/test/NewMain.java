/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.server.test;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mnunezpoggi.ssocket4j.server.Server;
import com.mnunezpoggi.ssocket4j.server.exceptions.PortNotSetException;
import com.mnunezpoggi.ssocket4j.server.handlers.OnConnectHandler;
import com.mnunezpoggi.ssocket4j.server.handlers.OnDisconnectHandler;
import com.mnunezpoggi.ssocket4j.server.handlers.OnReceiveHandler;

/**
 *
 * @author radxt
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Server s = new Server();
            s.startServer(5555);
            s.addOnReceiveHandler((String message, Integer key) -> {
                System.out.println("Recibido: " + message);
                if(message.equals("enviar"))
                        s.sendMessage("recibido",key);
            });

            s.addOnDisconnectHandler((Integer key) -> {
                System.out.println("Desconectado: " + key);
            });
            s.addOnConnectHandler((Integer key) -> {
                System.out.println("Conectado: " + key);
            });
            new Thread(new ReadRunnable(s)).start();
        } catch (IOException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PortNotSetException ex) {
            Logger.getLogger(NewMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static class ReadRunnable implements Runnable {
        private Server s;
        
        public ReadRunnable(Server s){
            this.s = s;
        }
    @Override
    public void run() {
        final Scanner in = new Scanner(System.in);
        while(in.hasNext()) {
            final String line = in.nextLine();
            s.sendMessage(line);
            if ("end".equalsIgnoreCase(line)) {
                System.out.println("Ending one thread");
                break;
            }
        }
    }

}

}
