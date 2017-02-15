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
public class User implements Runnable, Observer {

    private Socket socket;
    private String username;
    private BufferedReader reader;
    private OutputStream writer;
    private boolean connected;

    public User(Socket socket, String username) {
        this.socket = socket;
        this.username = username;

        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = this.socket.getOutputStream();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        connected = true;
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
        try {
            writer.write((msg + "\n").getBytes());
            writer.flush();
        } catch (IOException e) {
            connected = false;
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isConnected() {
        // http://stackoverflow.com/a/11736683
        // SOCKETS ARE STUPID!?!?!?!

        // waits until disconnected..
        //return socket.getInputStream().read() != -1;

        /*
        try {
            // This method works but is fucked up like everything about a socket
            socket.getOutputStream().write(new byte[1]);
            socket.getOutputStream().flush();
            socket.getOutputStream().write(new byte[1]);
            socket.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
        */

        // FUCK SOCKETS!
        return connected;
    }
}
