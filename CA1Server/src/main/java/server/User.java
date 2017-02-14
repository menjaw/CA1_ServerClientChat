package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
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
    private InputStream inputStream;
    private OutputStream outputStream;

    public User(Socket socket, String username) {
        this.socket = socket;
        this.username = username.toLowerCase();
    }

    public InputStream getInputStream() {
        try {
            if (inputStream == null)
                inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    public OutputStream getOutputStream() {
        try {
            if (outputStream == null)
                outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void update(Observable o, Object arg) {
        User u = null;
        if (arg instanceof User)
            u = (User) arg;

        if (u == null || u == this) return;

        PrintWriter writer = new PrintWriter(outputStream);
        writer.flush();
        writer.println("UPDATE#" + u.username);
        writer.flush();
    }
}
