package server;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

import static org.junit.Assert.assertTrue;

/**
 * Created by Niki on 2017-02-17.
 *
 * @author Niki
 */
public class ChatServerTest {


    private ChatServer cs;
    private Socket s1;

    private Scanner r1;
    private PrintWriter w1;
    private Random random = new Random();
    private int serverPort;

    @Before
    public void setUp() throws Exception {
        serverPort = random.nextInt(1000) + 6000;
        cs = new ChatServer("localhost", serverPort);
        new Thread(() -> {
            try {
                cs.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        s1 = new Socket();
        s1.connect(new InetSocketAddress("localhost", serverPort));

        r1 = new Scanner(s1.getInputStream());
        w1 = new PrintWriter(s1.getOutputStream(), true);
    }

    @Test
    public void startServer() throws Exception {
        assertTrue(s1.isConnected());
    }

    @Test
    public void login() throws Exception {
        w1.println("LOGIN#S1");
        String s = r1.nextLine();
        //System.out.println(s);
        assertTrue("server response should contain 'OK'", s.contains
                ("OK"));
    }

    @Test
    public void updateClient() throws Exception {
        Socket s2 = new Socket();
        s2.connect(new InetSocketAddress("localhost", serverPort));

        Scanner r2 = new Scanner(s2.getInputStream());
        PrintWriter w2 = new PrintWriter(s2.getOutputStream(), true);

        w1.println("LOGIN#updateClient(1)");
        r1.nextLine();

        w2.println("LOGIN#updateClient(2)");
        String res1, res2;

        if (r2.hasNextLine()) {
            res2 = r2.nextLine();
            //System.out.println(res2);
            assertTrue(res2.contains("updateClient(1)"));
        }

        if (r1.hasNextLine()) {
            res1 = r1.nextLine();
            //System.out.println(res1);
            assertTrue(res1.contains("updateClient(2)"));
        }
    }

    @Test
    public void messageAll() throws Exception {
        w1.println("LOGIN#messageAll()");
        r1.nextLine();
        w1.println("MSG#ALL#test all message");
        String s = r1.nextLine();
        //System.out.println(s);
        assertTrue(s.contains("MSG#messageAll()#test all message"));
    }

    @Test
    public void messageSelf() throws Exception {
        w1.println("LOGIN#messageSelf()");
        r1.nextLine();
        w1.println("MSG#messageSelf#test self message");
        String s = r1.nextLine();
        //System.out.println(s);
        assertTrue(s.contains("MSG#messageSelf()#test self message"));
    }

    @Test
    public void messageOther() throws Exception {
        /*Socket s2 = new Socket();
        s2.connect(new InetSocketAddress("localhost", serverPort));

        Scanner r2 = new Scanner(s2.getInputStream());
        PrintWriter w2 = new PrintWriter(s2.getOutputStream(), true);

        w1.println("LOGIN#messageOther(1)");
        r1.nextLine();

        w2.println("LOGIN#messageOther(2)");
        String res1, res2;

        if (r2.hasNextLine()) {
            res2 = r2.nextLine();
            //System.out.println(res2);
            assertTrue(res2.contains("updateClient(1)"));
        }

        if (r1.hasNextLine()) {
            res1 = r1.nextLine();
            //System.out.println(res1);
            assertTrue(res1.contains("updateClient(2)"));
        }*/
    }
}