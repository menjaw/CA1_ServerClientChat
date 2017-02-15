package server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Jamie
 */
public class ChatServer extends Observable {

    private final String host;
    private final int port;
    public static ArrayList<User> users = new ArrayList<>();

    

    private static ExecutorService executor = Executors.newCachedThreadPool();

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
        executor.execute(new MessageConsumer());
        Socket connection;
        while ((connection = socket.accept()) != null) {
            handleConnection(connection);
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
        boolean nameExists = false;
        String[] strings = line.split("#");

        if (strings.length >= 1
                && strings[0].equalsIgnoreCase("LOGIN")) {

            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(strings[1])) {
                    writer.println("FAIL");
                    nameExists = true;
                    break;
                }
            }
            if (nameExists) {
                connection.close();
                return;
            }
            User newGuy = new User(connection, strings[1]);
            users.add(newGuy);
            addObserver(newGuy);
            setChanged();
            notifyObservers(newGuy);
            executor.execute(newGuy);

            String okMsg = "OK";
            for (User user : users) {
                okMsg += "#" + user.getUsername();
            }
            newGuy.write(okMsg);

        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer("localhost", 8081);
        server.startServer();
    }
}
