package ClientViewControler;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bruger
 */
public class ClientJUnitTest {

    public static ClientControler cc;
    public static String inputServer;
    public static Thread input;

    public ClientJUnitTest() {

    }

    @BeforeClass
    public static void setUpClass() {

        String host = "localhost";
        int port = 8081;
        ServerSocket socket;
        try {

            socket = new ServerSocket();
            socket.bind(new InetSocketAddress(host, port));

            input = new Thread(() -> {
                try {
                    Socket s;
                    while ((s = socket.accept()) != null) {

                        Scanner scan = new Scanner(s.getInputStream());
                        inputServer = scan.nextLine();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ClientJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
            input.start();
        } catch (IOException ex) {
            Logger.getLogger(ClientJUnitTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @AfterClass
    public static void tearDownClass() {
        input.interrupt();
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void login() throws InterruptedException {
        cc = new ClientControler();
        cc.login("mahnaz");
        Thread.sleep(1000);
        String[] s = inputServer.split("#");
        assertTrue(s[0].equals("LOGIN"));

    }

    @Test
    public void sendBesked() throws InterruptedException {
     cc = new ClientControler();
        cc.sendBesked("mahnaz","hej");
        Thread.sleep(1000);
        String[] s = inputServer.split("#");
        assertTrue(s[0].equals("MSG"));

    }

}
