/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Menja
 */
public class MessageListener_Menja extends Thread {

    ServerSocket serverSocket;
    int port;
    WritableGUI_Menja gui;

    //constructors
    public MessageListener_Menja() {
        try {
            serverSocket = new ServerSocket();
        } catch (IOException ex) {
            Logger.getLogger(MessageListener_Menja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //constructors
    public MessageListener_Menja(WritableGUI_Menja gui, int port) {
        this.port = port;
        this.gui = gui;
        try {
            serverSocket = new ServerSocket();
        } catch (IOException ex) {
            Logger.getLogger(MessageListener_Menja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {

        try {
            //initilize the Socket variable
            //socket is a connection to the port
            Socket clientSocket;

            //while loop that keeps on the accepting connection 
            //when it gets the connection it put it in the clientSocket variable
            //if the connection is null or close, we will leave the while-loop
            while ((clientSocket = serverSocket.accept()) != null) {
                InputStream inputStream = clientSocket.getInputStream();
                
                Scanner reader = new Scanner(inputStream);
                
                String line = reader.nextLine();
                if (line != null) {
                    gui.write(line);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(MessageListener_Menja.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
