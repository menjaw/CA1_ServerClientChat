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
import java.util.Observable;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static BlockingQueue<Message> messages = new ArrayBlockingQueue<>(128);

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

        String[] strings = line.split("#");
        if (strings.length >= 1) {
            switch (strings[0]) {
                case "LOGIN":
                    for (User user : users) {
                        if (user.getUsername().equalsIgnoreCase(strings[1])) {
                            //TODO: Fail
                            break;
                        }
                    }
                    //TODO: setup new userSocket 
                    User newGuy = new User(connection, strings[1]);
                    users.add(newGuy);
                    addObserver(newGuy);
                    notifyObservers(newGuy);
                    executor.execute(newGuy);

                    
                    //TODO: OK message
                    String okMsg="OK#";
                    for (User user : users) {
                        okMsg.concat(user.getUsername()+"#");

                    }
                    newGuy.write(okMsg);
                    break;
                case "MSG":
                    //TODO: What happens here now?
                    break;
                default:
                    break;
            }
        }
    }

    public static class MessageConsumer implements Runnable {

        @Override
        public void run() {
            Message msg;
            while (true) {
                try {
                    msg = messages.take();
                    if (msg.getReceiver() == null) {
                        for (User user : users) {
                            user.write(msg.toString());

                        }

                    } else {
                        msg.getReceiver().write(msg.toString());

                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer server = new ChatServer("localhost", 8081);

        server.startServer();
    }

}
