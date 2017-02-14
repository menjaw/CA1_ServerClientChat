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

    public ChatServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * Starts running the server.
     *
     * @throws IOException If network or I/O or something goes wrong.
     */
    public void startServer() throws IOException {
        // Create a new unbound socket
        ServerSocket socket = new ServerSocket();
        // Bind to a port number
        socket.bind(new InetSocketAddress(host, port));

        System.out.println("Server listening on port " + port);

        // Wait for a connection
        Socket connection;
        while ((connection = socket.accept()) != null) {
            // Handle the connection in the #handleConnection method below
            handleConnection(connection);
            // Now the connection has been handled and we've sent our reply
            // -- So now the connection can be closed so we can open more
            //    sockets in the future
            connection.close();
        }
    }

    /**
     * Handles a connection from a client by simply echoing back the same thing
     * the client sent.
     *
     * @param connection The Socket connection which is connected to the client.
     * @throws IOException If network or I/O or something goes wrong.
     */
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
                    break;
                case "MSG":
                    switch (strings[1]) {
                        case "ALL":
                            writer.print("MSG#"+strings[2]);
                        default:
                            writer.print("MSG#"+strings[2]);
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

        // This method will block, forever!
        server.startServer();
    }

}
