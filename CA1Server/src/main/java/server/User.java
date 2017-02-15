package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Niki on 2017-02-14.
 *
 * @author Niki
 */
public class User implements Runnable, Observer {

    private Socket socket;
    private String username;
    private BufferedReader reader;
    private PrintStream writer;

    public User(Socket socket, String username) {
        this.socket = socket;
        this.username = username.toLowerCase();

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintStream(socket.getOutputStream());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Notification n = null;

        if (arg instanceof Notification) n = (Notification) arg;
        if (n == null || n.user == this) return;

        if (n.type == Notification.Type.UPDATE)
            write("UPDATE#" + n.user.username);
        else if (n.type == Notification.Type.DELETE)
            write("DELETE#" + n.user.username);
    }

    @Override
    public void run() {
        try {
            String s;
            while ((s = reader.readLine()) != null) {
                Message msg = new Message(this, s);
                MessageConsumer.messages.put(msg);

                System.out.printf("%s, %s, %s\n",
                                  msg.getSender().getUsername(),
                                  msg.getReceiver() != null ? msg.getReceiver
                                          ().getUsername() : "ALL",
                                  msg.getData()); // debug
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void write(String msg) {
        writer.println(msg);
        writer.flush();
    }

    public String getUsername() {
        return username;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintStream getWriter() {
        return writer;
    }

    public boolean isConnected() {
        return socket.isConnected();
    }
}
