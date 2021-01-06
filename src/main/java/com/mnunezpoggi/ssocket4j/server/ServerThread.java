package com.mnunezpoggi.ssocket4j.server;

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
public class ServerThread extends Thread {

    private Socket Socket_;
    //private DataInputStream InputStream_;
    private BufferedReader InputStream_;
 //   private DataOutputStream OutputStream_;
    private PrintWriter OutputStream_;
    private int Port_;
    private String Hostname_;
    private int Id_;

    /**
     * Upon creation it receives the socket that outputs the method accept()
     * from SocketServer. It sets the local port from the socket (not the port
     * set in Server), it sets the remote hostname for identification and
     * allocates Buffered DataOutputStream for reading big data.
     *
     * @param socket The raw connection between client and server
     */
    public ServerThread(Socket socket) {
        this.Socket_ = socket;
        this.Port_ = Socket_.getPort();
        this.Hostname_ = Socket_.getInetAddress().getHostAddress();
        try {
//            OutputStream_ = new DataOutputStream(
//                                new BufferedOutputStream(
//                                        Socket_.getOutputStream()));
            OutputStream_ = new PrintWriter(
                                    new BufferedWriter(
                                            new OutputStreamWriter(
                                                    Socket_.getOutputStream())));
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method is called when the client disconnects itself or is disconnected
     * by the server. It dispatchs any onDisconnect handlers avaiable,
     * removes any references of its corresponding ServerThread object and
     * stops this thread
     *
     */
    private void clean() {
        Components.dispatchOnDisconnect(Id_);
        close();
        Components.removeServerThread(Id_);
        try {
            this.finalize();
        } catch (Throwable ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

      /**
     * Calculates, sets and returns a unique hashcode from this object. It is
     * used as key in the other classes.
     *
     * @return int The calculated code
     */
    public int calculateId() {
        this.Id_ = this.hashCode();
        return Id_;
    }

     /**
     * Method that overrides Thread's own run(). Shouldn't be called explicitly
     * but from a class that accepts connection, like SecureServer or Server.
     * It initializes the Buffered InputStream that will be reading data incoming
     * from clients. Also starts a loop while there is connection that keeps reading
     * any data. When data arrives it dispatches any OnReceive listeners available.
     */
    @Override
    public void run() {
        try {
//            InputStream_ = new DataInputStream(
//                                new BufferedInputStream(
//                                        Socket_.getInputStream()));
            InputStream_ = new BufferedReader(
                                new InputStreamReader(Socket_.getInputStream()));
            while (Socket_.isConnected()) {
                String o = InputStream_.readLine();
                if (o == null){
                    clean();
                    break;
                }
                    
                int i = runSystemCommand(o);
                if (i == 1) {
                    continue;
                }
                Components.dispatchOnReceive(o, Id_);
            }
        } catch (EOFException e) {
            clean();
        } catch (IOException ex) {
            clean();
        } finally {
        }
    }

     /**
     * This connection's own sendMessage for sending a message to this specific
     * client. It shouldn't be acceded directly but from another data structure 
     * like a HashMap (faster) or ArrayList (For security rasons)
     *
     * @param message The message to be sent
     */
    public void sendMessage(String message) {
        OutputStream_.println(message);
        OutputStream_.flush();
    }

     /**
     *Method that closes the socket
     */
    private void close() {
        try {
            Socket_.close();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String toString() {
        String s = "";
        s = s + Hostname_ + " at port: " + Port_;
        return s;
    }

     /**
      * Executes any custom command.
     * @deprecated 
     */
    private int runSystemCommand(Object o) {
        try {
            String s = (String) o;
            if (s.equals("showConnections()")) {
                for(int key: Components.getAllKeys()){
                    System.out.println("Key: " + key + 
                            "ServerThread: " + Components.getServerThread(Id_));
                };
                return 1;
            }
        } catch (Exception e) {
            return 0;
        }
        return 0;
    }

}
