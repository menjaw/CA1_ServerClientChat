package io.skaarup;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * Created by Niki on 2017-02-16.
 *
 * @author Niki
 */
public class Client extends Observable {

    private String host;
    private int port;
    private String username;
    private Socket socket;
    private Scanner reader;
    private PrintWriter writer;
    private boolean connected;
    private boolean running;

    private List<String> users;

    /**
     * This class extends Runnable and should be run in a Thread otherwise it
     * will be stuck in an infinite loop while.
     *
     * @param host     the IP address of the server, you are trying to
     *                 connect to
     * @param port     the Port which the server listens on
     * @param username the username which to login as
     */
    public Client(String host, int port, String username) {
        this.host = host;
        this.port = port;
        this.username = username;
    }

    /**
     * Starts a new thread and runs initConnection() in it
     */
    public void connect() {
        new Thread(() -> {
            try {
                initConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * runs initConnection() in the ExecutorService supplied
     */
    public void connect(ExecutorService es) {
        es.execute(() -> {
            try {
                initConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Sets up the initial connection and begins receiving messages immediately
     */
    private void initConnection() throws IOException {
        users = new CopyOnWriteArrayList<>();
        socket = new Socket();
        connected = false;

        socket.connect(new InetSocketAddress(host, port));

        reader = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);

        writer.println("LOGIN#" + username);

        String temp = reader.nextLine();
        String[] temp2 = temp.split("#");

        if (temp2.length > 0 && temp2[0].equalsIgnoreCase("OK")) {
            if (temp2.length > 1)
                users.addAll(Arrays.asList(temp2).subList(1, temp2.length));
            connected = true;
            receiver();
        } else {
            // Login seems to have failed!
        }
    }

    /**
     * io.skaarup.Message receiving loop
     */
    private void receiver() {
        String s;
        Notification n;
        while ((s = reader.nextLine()) != null) {
            String[] temp = s.split("#");
            if (temp.length < 2) {
                System.out.println("Continued: UPDATE/DELETE");
                continue;
            }

            if (temp[0].equalsIgnoreCase("UPDATE")) {
                addUser(temp);
                n = new Notification(Notification.Type.UPDATE, temp[1]);
                setChanged();
                notifyObservers(n);
                continue;
            }
            if (temp[0].equalsIgnoreCase("DELETE")) {
                delUser(temp);
                n = new Notification(Notification.Type.DELETE, temp[1]);
                setChanged();
                notifyObservers(n);
                continue;
            }

            if (temp.length < 3) {
                System.out.println("Continued: MESSAGE");
                continue;
            }
            if (temp[0].equalsIgnoreCase("MSG")) {
                String username = temp[1];
                String data = temp[2];
                Message message = new Message(username, data);
                n = new Notification(Notification.Type.MESSAGE, message);
                setChanged();
                notifyObservers(n);
            }
        }
    }

    private synchronized void addUser(String[] sa) {
        if (sa.length >= 2 && !users.contains(sa[1]))
            users.add(sa[1]);
    }

    private synchronized void delUser(String[] sa) {
        if (sa.length >= 2 && users.contains(sa[1]))
            users.remove(sa[1]);
    }

    public List<String> getUsers() {
        return users;
    }

    /**
     * send a message to everyone
     *
     * @param text text
     */
    public void sendToAll(String text) {
        writer.println("MSG#ALL#" + text);
    }

    /**
     * send a message to a single user
     *
     * @param text     text
     * @param username the name of the user that should be whispered to
     */
    public void sendWhisper(String text, String username) {
        writer.println("MSG#" + username + "#" + text);
    }

    /**
     * Auto detect whether a message should be send as private or public
     * If a io.skaarup.Message contains a username it will be sent to that user, if not
     * it will be sent to all users
     *
     * @param msg the message which is to be sent
     */
    public void sendMessage(Message msg) {
        if (msg.isMessagePrivate())
            sendWhisper(msg.text, msg.username);
        else
            sendToAll(msg.text);
    }

    public boolean isConnected() {
        return connected;
    }

}
