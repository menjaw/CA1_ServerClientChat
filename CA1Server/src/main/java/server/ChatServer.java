package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Jamie
 */
public class ChatServer extends Observable {

    private final String host;
    private final int port;
    public static List<User> users = new CopyOnWriteArrayList<>();

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

        //executor.execute(new DeleteConsumer(this));
        Socket connection;
        while ((connection = socket.accept()) != null) {
            handleConnection(connection);
        }
    }

    private void handleConnection(Socket connection) throws IOException {
        OutputStream output = connection.getOutputStream();
        InputStream input = connection.getInputStream();

        // Read whatever comes in
        Scanner reader = new Scanner(input);
        String line = reader.nextLine();

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

            setChangedAndNotify(new Notification(newGuy, Notification.Type
                    .UPDATE));

            String okMsg = "OK";
            for (User user : users) {
                okMsg += "#" + user.getUsername();
            }
            newGuy.write(okMsg);

            addObserver(newGuy);//??
            users.add(newGuy);
            executor.execute(newGuy);//??

        }
    }

    public synchronized void setChangedAndNotify(Notification n) {
        setChanged();
        notifyObservers(n);
    }

    public static ChatServer server;

    public static void main(String[] args) throws IOException {
        String host = "localhost";
        if (args.length > 0)
            host = args[0];

        server = new ChatServer(host, 8081);
        server.startServer();
    }

    public synchronized static void removeUser(User user) {
        server.deleteObserver(user);
        users.remove(user);
        server.setChangedAndNotify(
                new Notification(user, Notification.Type.DELETE));
    }
}
