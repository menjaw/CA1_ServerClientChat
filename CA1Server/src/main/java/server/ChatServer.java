package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jamie
 */
public class ChatServer {

    private final String host;
    private final int port;
    private ArrayList<User> users= new ArrayList<>();

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startServer() throws IOException {
        // Create a new unbound socket
        ServerSocket socket = new ServerSocket();
        // Bind to a port number
        socket.bind(new InetSocketAddress(host, port));

        System.out.println("Server listening on port " + port);
        Socket connection;
        while ((connection = socket.accept()) != null) {
            handleConnection(connection);
            connection.close();
        }
    }

    private void handleConnection(Socket connection) throws IOException {
        OutputStream output = connection.getOutputStream();
        InputStream input = connection.getInputStream();

        // Read whatever comes in
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = reader.readLine();

        // Print the same line we read to the client
        PrintStream writer = new PrintStream(output);

        String[] strings = line.split("#");
        if (strings.length >= 1) {
            switch (strings[0]) {
                case "LOGIN":
                    //TODO: setup new userSocket 
                    //TODO: add new user to userlist
                    users.add(new User(connection, strings[1]));

                    writer.print("UPDATE#" + strings[1]);
                    break;
                case "MSG":
                    switch (strings[1]) {
                        case "ALL":
                            writer.print("MSG#" + "[Sender]#" + strings[2]);
                        default:
                            writer.print("MSG#" + "[Sender]#" + strings[2]);
                    }
                    break;
                default:
                    break;
            }
        }
        connection.close();
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer("localhost", 8080);

        server.startServer();
    }

}
