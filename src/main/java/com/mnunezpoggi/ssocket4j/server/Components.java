package com.mnunezpoggi.ssocket4j.server;

/**
 *
 * @author mauricio
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import com.mnunezpoggi.ssocket4j.server.exceptions.PortNotSetException;
import com.mnunezpoggi.ssocket4j.server.handlers.OnConnectHandler;
import com.mnunezpoggi.ssocket4j.server.handlers.OnDisconnectHandler;
import com.mnunezpoggi.ssocket4j.server.handlers.OnReceiveHandler;

/**
 * Normally acceded by the Server class. This is where the main components
 * resides statically.
 *
 *
 * @author mauricio
 * @version 0.1
 * @see java.net.Socket
 * @since 0.01
 *
 */
public class Components {

    private static int PORT = 0;

    private static ServerSocket SERVER_SOCKET;
    private static ConcurrentHashMap<Integer, ServerThread> CLIENTS;

    private static ArrayList<OnConnectHandler> ONCONNECT_HANDLER_LIST;
    private static ArrayList<OnDisconnectHandler> ONDISCONNECT_HANDLER_LIST;
    private static ArrayList<OnReceiveHandler> ONRECEIVE_HANDLER_LIST;

    /**
     * Initializes empty data structures for handling Clients connections and
     * handlers
     *
     */
    private static void initializeHandlersAndClients() {
        CLIENTS = new ConcurrentHashMap();
        ONCONNECT_HANDLER_LIST = new ArrayList();
        ONDISCONNECT_HANDLER_LIST = new ArrayList();
        ONRECEIVE_HANDLER_LIST = new ArrayList();
    }

    /**
     * Checks if port is set
     *
     * @throws PortNotSetException If port is not set
     */
    private static void checkPort() throws PortNotSetException {
        if (PORT == 0) {
            throw new PortNotSetException();
        }
    }

    /**
     * Sets new port for listening to
     *
     * @param port Unsigned integer for listening to.
     */
    protected static void setPort(int port) {
        PORT = port;
    }

    /**
     * Creates new ServerSocket which starts listening on specified port
     *
     * @throws IOException If port is already used, if port is reserved or if
     * network access is denied
     * @throws PortNotSetException If port is not set on method setPort(int
     * Port)
     */
    protected static void initializeServerSocket() throws IOException,
            PortNotSetException {
        checkPort();
        initializeHandlersAndClients();
        SERVER_SOCKET = new ServerSocket(PORT);
    }

    /**
     * Same as initializeServerSocket() but instead of creating a simple
     * ServerSocket it creates an SSLServerSocket for encrypted connections
     *
     * @param keystorePath Path of file storing the server's private keys
     * @param keystorePassword Password for accessing the key store
     * @throws IOException If port is already used, if port is reserved or if
     * network access is denied
     * @throws PortNotSetException If port is not set on method setPort(int
     * Port)
     * 
     */
    
    protected static void initializeSecureServerSocket(String keystorePath,
                                                 String keystorePassword)
                                                    throws IOException,
                                                           PortNotSetException {
        InitializeSecureProperties(keystorePath, keystorePassword);
        checkPort();
        initializeHandlersAndClients();
        ServerSocketFactory serverSocketFactory = SSLServerSocketFactory.getDefault();
        SERVER_SOCKET = serverSocketFactory.createServerSocket(PORT);
    }
    
    /**
     * Sets System properties for the server to recognize keystore and its 
     * password
     * @param keystorePath Path of file storing the server's private keys
     * @param keystorePassword Password for accessing the key store
     * @see keytool
     * 
     */
    
    private static void InitializeSecureProperties(String keystorePath, String keystorePassword){
        System.setProperty("javax.net.ssl.keyStore",
                keystorePath);
        System.setProperty("javax.net.ssl.keyStorePassword",
                keystorePassword);
            }
        
     /**
     * Getter for ServerSocket
     *
     * @return ServerSocket Returns the actual ServerSocket instance
     */
    protected static ServerSocket getServerSocket() {
        return SERVER_SOCKET;
    }

     /**
     * Adds Handler for managing whenever a new client connects
     *
     * @param handler Implementation of the OnConnectHandler interface
     */
    protected static void addOnConnectHandler(OnConnectHandler handler) {
        ONCONNECT_HANDLER_LIST.add(handler);
    }

     /**
     * Adds Handler for managing whenever a new client disconnects itself 
     * from the server
     *
     * @param handler Implementation of the OnDisconnectHandler interface
     */
    protected static void addOnDisconnectHandler(OnDisconnectHandler handler) {
        ONDISCONNECT_HANDLER_LIST.add(handler);
    }
    
     /**
     * Adds Handler for managing whenever a new client sends a message
     * the server
     *
     * @param handler Implementation of the OnReceiveHandler interface
     */
    protected static void addOnReceiveHandler(OnReceiveHandler handler) {
        ONRECEIVE_HANDLER_LIST.add(handler);
    }

    /**
     * Removes a specified OnConnectHandler
     *
     * @param handler Implementation of the OnConnectHandler interface
     * to be removed
     */
    protected static void removeOnConnectHandler(OnConnectHandler handler) {
        ONCONNECT_HANDLER_LIST.remove(handler);
    }
    
     /**
     * Removes a specified OnDisconnectHandler
     *
     * @param handler Implementation of the OnDisconnectHandler interface
     * to be removed
     */
    protected static void removeOnDisconnectHandler(OnDisconnectHandler handler) {
        ONDISCONNECT_HANDLER_LIST.remove(handler);
    }

      /**
     * Removes a specified OnConnectHandler
     *
     * @param handler Implementation of the OnConnectHandler interface
     * to be removed
     */
    protected static void removeOnReceiveHandler(OnReceiveHandler handler) {
        ONRECEIVE_HANDLER_LIST.remove(handler);
    }

      /**
     * Returns the Thread on which a specific client is connected to given 
     * its id
     *
     * @param id The auto-generated Id of the connection
     * @return ServerThread The ServerThread  on which a specific client
     *          is connected to
     */
    protected static ServerThread getServerThread(int id) {
        return CLIENTS.get(id);
    }

      /**
     * Returns all the atuo-generated keys for the clients. In other words
     * return all the id's for all the clients
     *
     * @return Set A Iterable set of Keys (Integer)
     */
    protected static Set<Integer> getAllKeys() {
        return CLIENTS.keySet();
    }

    /**
     * Adds a new thread containing a new connection to the connection pool
     * to be managed 
     *
     * @param serverThread The already created thread
     * @return Integer The unique id of the connection
     */
    protected static synchronized int addServerThread(ServerThread serverThread) {
        int id;
        id = serverThread.calculateId();
        CLIENTS.put(id, serverThread);
        serverThread.start();
        return id;
    }

    /**
     * Removes  a thread containing a connection specified by its id.
     * Used when a client disconnects itself from the server.
     * 
     * @param key The unique Id of the connection
     */
    protected static synchronized void removeServerThread(int key) {
        CLIENTS.remove(key);
    }

    /**
     * Notifies all the OnDisconnect handlers to execute is method
     * onDisconnect(key) because a client has disconnected from the server
     * 
     * @param key The unique Id of the connection
     */
    protected static void dispatchOnDisconnect(Integer key) {
        for (OnDisconnectHandler handler : ONDISCONNECT_HANDLER_LIST) {
            handler.onDisconnect(key);
        }
    }

     /**
     * Notifies all the OnReceive handlers to execute is method
     * onReceive(messsage, key) because a client has sent a message to the server
     * 
     * @param message The message sent by the client
     * @param key The unique Id of the connection
     */
    protected static void dispatchOnReceive(String message, Integer key) {
        for (OnReceiveHandler handler : ONRECEIVE_HANDLER_LIST) {
            handler.onReceive(message, key);
        }
    }

     /**
     * Notifies all the OnConnect handlers to execute is method
     * onConnect(key) because a client has connected to the server
     * 
     * @param key The unique Id of the connection
     */
    protected static void dispatchOnConnect(Integer key) {
        for (OnConnectHandler handler : ONCONNECT_HANDLER_LIST) {
            handler.onConnect(key);
        }
    }

}
