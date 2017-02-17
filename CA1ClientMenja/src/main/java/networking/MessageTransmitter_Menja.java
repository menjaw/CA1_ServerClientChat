/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Menja
 */
public class MessageTransmitter_Menja extends Thread {

    String message;
    String host;
    int port;
    Socket socket;

    //constructors
    public MessageTransmitter_Menja() {
    }

    public MessageTransmitter_Menja(String host, int port) {
        this.host = host;
        this.port = port;
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter_Menja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        write("MSG#ALL#" + message);
    }
    
    private synchronized void write(String s) {
        try {
            socket.getOutputStream().write(s.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(MessageTransmitter_Menja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
