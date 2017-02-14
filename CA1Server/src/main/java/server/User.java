package server;

import java.io.*;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Niki on 2017-02-14.
 *
 * @author Niki
 */
public class User implements Observer {

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
        User u = null;
        if (arg instanceof User) u = (User) arg;
        if (u == null || u == this) return;

        writer.println("UPDATE#" + u.username);
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
}
