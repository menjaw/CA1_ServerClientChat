package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Observable;
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

    public static ExecutorService executor = Executors.newCachedThreadPool();

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
            /*
                TODO: rewrite HandleConnection to enter a new thread quickly
                or make it so user can be created from a socket and handle it
                when handing User over to ExecutorService
             */
            executor.execute(new connectionHandler(connection,this));
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
