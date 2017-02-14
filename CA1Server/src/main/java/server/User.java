package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Niki on 2017-02-14.
 *
 * @author Niki
 */
public class User {

    private Socket socket;
    private String username;
    private InputStream inputStream;
    private OutputStream outputStream;

    public User(Socket socket, String username) {
        this.socket = socket;
        this.username = username;
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
}
