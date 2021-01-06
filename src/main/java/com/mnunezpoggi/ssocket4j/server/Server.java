package com.mnunezpoggi.ssocket4j.server;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mnunezpoggi.ssocket4j.server.exceptions.PortNotSetException;
import com.mnunezpoggi.ssocket4j.server.handlers.OnConnectHandler;
import com.mnunezpoggi.ssocket4j.server.handlers.OnDisconnectHandler;
import com.mnunezpoggi.ssocket4j.server.handlers.OnReceiveHandler;

/**
 * Base class enough for most users which doesn't need encryption.
 * Provides methods for starting a new Socket Server that listens on a specified
 * port
 * 1. Instantiate new server
 * eg: Server s = new Server(9990);   //In this case 9990 is the port that
 *                                   // the server will be listening on
 * 2. Add the Handlers you want
 * eg: s.addOnConnectHandler(new OnConnectHandler(){
 *                                    //Here comes the implementation                                      
 *                                             });
 * 
 * 3. 
 *
 * @author mauricio
 * @version 0.1
 * @see java.net.Socket
 * @since 0.01
 *
 */
public class Server extends Thread {

        /**
     * Main method for starting an unsecure server.
     * 
     * @param port The port on which the server will be listening
     * @throws IOException If There is not enough permissions or if the
     *                              port is already used.
     * @throws PortNotSetException If there is no port set
     * 
     */
    
    public void startServer(int port) throws IOException, PortNotSetException{
        Components.setPort(port);
        Components.initializeServerSocket();
        start();
    }

    /**
     * Adds Handler for managing whenever a new client connects
     *
     * @param handler Implementation of the OnConnectHandler interface
     */
    public synchronized void addOnConnectHandler(OnConnectHandler handler){
        Components.addOnConnectHandler(handler);
    }
    
    /**
     * Adds Handler for managing whenever a new client disconnects itself 
     * from the server
     *
     * @param handler Implementation of the OnDisconnectHandler interface
     */
      public synchronized void addOnDisconnectHandler(OnDisconnectHandler handler) {
        Components.addOnDisconnectHandler(handler);
    }
     
       /**
     * Adds Handler for managing whenever a new client sends a message
     * the server
     *
     * @param handler Implementation of the OnReceiveHandler interface
     */
          public synchronized void addOnReceiveHandler(OnReceiveHandler handler) {
        Components.addOnReceiveHandler(handler);
    }

     /**
     * Starts the thread, and keeps always listening to new connections
     * it adds every new connection to the connection pool 
     * and finally it dispatches any onConnect handlers available
     * 
     *  @deprecated 
     *  @see Thread
     */
    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = Components.getServerSocket()
                                                            .accept();
                ServerThread serverThread = new ServerThread(socket);
                int id = Components.addServerThread(serverThread);
                Components.dispatchOnConnect(id);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    
 /**
     * Method designed to send specific commands to the server
     * 
     * @param command The command to be executed
     * @param key The client
     *  @deprecated 
     */
    public void executeCommand(String command, int key) {
        Components.getServerThread(key).sendMessage(command);
    }

    /**
     * Method to send Message to ALL connected clients
     * USE WITH CAUTION
     * It may produce a hang or bottleneck if bandwidth is limited
     * 
     * @param message The message to be sent to all clients
     */
    public synchronized void sendMessage(String message) {
        for (Integer key : Components.getAllKeys()) {
            sendMessage(message, key);
        }
    }
    
     /**
     * Method to send a message to a specific client identified by key
   
     * @param message The message to be sent
     * @param key The unique Id of the client that will receive the message
     */
    public void sendMessage(String message, Integer key) {
        ServerThread st = Components.getServerThread(key);
        st.sendMessage(message);
    }
    
     /**
     * Method to remove or disconnect a client from the server. Mainly for
     * security reasons
     * 
     * @param key The unique Id of the client that will be disconnected
     */
    public synchronized void removeClient(Integer key) {
        Components.removeServerThread(key);
    }
    
     /**
     * Gets all clients Id's
     * 
     * @return Object[] An array of objects
     */
    public Object[] getClients() {
        return Components.getAllKeys().toArray();
    }


}
