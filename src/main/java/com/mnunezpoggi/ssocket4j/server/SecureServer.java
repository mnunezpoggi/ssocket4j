package com.mnunezpoggi.ssocket4j.server;

import java.io.IOException;
import com.mnunezpoggi.ssocket4j.server.exceptions.PortNotSetException;

/**
 * Class that extends main Server class for enabling it to accept and
 * establish secure connections based on standard SSL/TLS 1.0 Encryption
 * Apart from encryption it works exactly the same as plain Server
 *
 * @author mauricio
 * @version 0.1
 * @see Server
 * @since 0.01
 *
 */
public class SecureServer extends Server{
    
     /**
     * Creates new ServerSocket which starts listening on specified port
     * and initializes secure connections.
     *
     * @param port The port in which the server will be listening to
     * @param keystorePath The path of the private certificates of the server
     * @param keystorePassword The password of the kestore
     * @throws IOException If port is already used, if port is reserved or if
     * network access is denied
     * @throws PortNotSetException If port is not set on method setPort(int
     * Port)
     */  
    
      public void startSecureServer(int port,String keystorePath, String keystorePassword) throws IOException, PortNotSetException{
        Components.setPort(port);
        Components.initializeSecureServerSocket(keystorePath, keystorePassword);
        start();
    }
    
    
    
}
