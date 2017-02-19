package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Niki on 2017-02-19.
 *
 * @author Niki
 */
public class FakeClient {

    public Socket socket;
    private final String hostName;
    private final int port;

    public BlockingQueue<String> messages;

    private Scanner reader;
    public PrintWriter writer;

    private boolean running;

    public FakeClient(String hostName, int port) {
        this.hostName = hostName;
        this.port = port;
        messages = new ArrayBlockingQueue<>(128);
        socket = new Socket();
    }

    public void connect() throws IOException {
        socket.connect(new InetSocketAddress(hostName, port));
        writer = new PrintWriter(socket.getOutputStream(), true);
        running = true;
        setupReceiver();
    }

    private void setupReceiver() {
        Thread t = new Thread(() -> {
            if(!running) return;
            try {
                reader = new Scanner(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s;
            while (running && (s = reader.nextLine()) != null) {
                if (!messages.offer(s)) {
                    System.out.println("too many messages in "
                                               + this.getClass().getName()
                                               + " ArrayBlockingQueue");
                }
            }
        });
        t.start();
    }


    public void disconnect() throws IOException {
        running = false;
        socket.close();
    }

}
