///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package view;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.Socket;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
///**
// *
// * @author Menja
// */
//public class Client {
//
//    //GUI
//    private GuiClient guiClient;
//
//    //Input (to read from the socket) and Output (to write from the socket)
//    private ObjectInputStream messageInput;
//    private ObjectOutputStream messageOutput;
//    private Socket socket;
//
//    //Login
//    private String username;
//    private String server;
//    private int port;
//
//    /**
//     * Consol constructor called by consol mode In this mode the GUI is set to
//     * null
//     *
//     * @param username
//     * @param server
//     * @param port
//     */
//    public Client(String username, String server, int port) {
//        this(username, server, port, null);
//    }
//
//    /**
//     * GUI constructor called by GUI mode
//     *
//     * @param username
//     * @param server: the address of the server
//     * @param port: the port number on the server
//     * @param guiClient:
//     */
//    public Client(String username, String server, int port, GuiClient guiClient) {
//        this.username = username;
//        this.server = server;
//        this.port = port;
//        this.guiClient = guiClient;
//    }
//
//    //Start and connect to the server
//    public boolean start() {
//        
//        //connect to server
//        try {
//            socket = new Socket(server, port);
//        } catch (IOException ex) {
//            //if the connection fail
//            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
//            System.out.println("Connection failed");
//            return false;
//        }
//        //if the connection succeeded
//        String serverConnected = "Connection succeeded " + "\n" + socket.getInetAddress() + ": " + socket.getPort();
//        System.out.println(serverConnected);
//        
//        
//        
//        
//        return true;
//    }
//}
