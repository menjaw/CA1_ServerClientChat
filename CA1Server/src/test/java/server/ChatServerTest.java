package server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Niki on 2017-02-17.
 *
 * @author Niki
 */
public class ChatServerTest {

    private static ChatServer cs = null;
    private Random random = new Random();
    private int serverPort;
    private static String hostName = "localhost";

    @Before
    public void setUp() throws Exception {
        serverPort = random.nextInt(1000) + 7000; // Multiple CS
        //serverPort = 7890; // Single CS
        //if(cs == null) { // Single CS
        cs = new ChatServer(hostName, serverPort);
        Thread t = new Thread(() -> {
            try {
                cs.startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        t.start();
        //} // Single CS
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void startServer() throws Exception {
        FakeClient fc = new FakeClient(hostName, serverPort);
        fc.connect();
        assertTrue(fc.socket.isConnected());
        fc.writer.println("LOGIN#startServer()");
        fc.disconnect();
    }

    @Test
    public void loginCommand() throws Exception {
        FakeClient fc = new FakeClient(hostName, serverPort);
        fc.connect();
        fc.writer.println("LOGIN#login()");
        assertFalse("server response should not contain 'FALSE'",
                    fc.messages.take().contains("FALSE"));
        fc.disconnect();
    }

    @Test
    public void updateCommand() throws Exception {
        FakeClient fc1 = new FakeClient(hostName, serverPort);
        fc1.connect();

        FakeClient fc2 = new FakeClient(hostName, serverPort);
        fc2.connect();

        fc1.writer.println("LOGIN#updateClient(1)");
        fc1.messages.take(); // clear OK#

        fc2.writer.println("LOGIN#updateClient(2)");
        fc2.messages.take(); // clear OK#

        assertTrue(fc1.messages.take().equals("UPDATE#updateClient(2)"));

        fc1.disconnect();
        fc2.disconnect();
    }

    @Test
    public void okCommand() throws Exception {
        FakeClient fc = new FakeClient(hostName, serverPort);
        fc.connect();

        fc.writer.println("LOGIN#okCommand()");
        String msg = fc.messages.take();
        assertTrue(msg.startsWith("OK#"));
        assertTrue(msg.contains("okCommand()"));
        fc.disconnect();
    }

    @Test
    public void msgAllCommand() throws Exception {
        FakeClient fc = new FakeClient(hostName, serverPort);
        fc.connect();
        fc.writer.println("LOGIN#messageAll()");
        fc.messages.take();
        fc.writer.println("MSG#ALL#test all message");
        assertTrue(fc.messages.take()
                              .equals("MSG#messageAll()#test all message"));
        fc.disconnect();
    }

    @Test
    public void msgSelfCommand() throws Exception {
        FakeClient fc = new FakeClient(hostName, serverPort);
        fc.connect();
        fc.writer.println("LOGIN#messageSelf()");
        fc.messages.take(); // clear OK!
        fc.writer.println("MSG#messageSelf#test self message");
        assertTrue(fc.messages.take()
                              .equals("MSG#messageSelf()#test self message"));
        fc.disconnect();
    }

    @Test
    public void msgOtherCommand() throws Exception {
        FakeClient fc1 = new FakeClient(hostName, serverPort);
        fc1.connect();

        FakeClient fc2 = new FakeClient(hostName, serverPort);
        fc2.connect();

        fc1.writer.println("LOGIN#messageOther(1)");
        fc1.messages.take(); // clear OK#

        fc2.writer.println("LOGIN#messageOther(2)");
        fc2.messages.take(); // clear OK#
        fc1.messages.take(); // clear UPDATE#

        // Send message from FakeClient 1 to 2
        fc1.writer.println("MSG#messageOther(2)#Hello1");
        fc1.messages.take(); // clear own message
        assertTrue(fc2.messages.take()
                               .equals("MSG#messageOther(1)#Hello1"));

        // Send message from FakeClient 2 to 1
        fc2.writer.println("MSG#messageOther(1)#Hello2");
        fc2.messages.take(); // clear own message
        assertTrue(fc1.messages.take()
                               .equals("MSG#messageOther(2)#Hello2"));
        fc1.disconnect();
        fc2.disconnect();
    }

    @Test
    public void deleteCommand() throws Exception {
        FakeClient fc1 = new FakeClient(hostName, serverPort);
        fc1.connect();

        FakeClient fc2 = new FakeClient(hostName, serverPort);
        fc2.connect();

        fc1.writer.println("LOGIN#deleteCommand(1)");
        fc1.messages.take(); // clear OK#

        fc2.writer.println("LOGIN#deleteCommand(2)");
        fc2.messages.take(); // clear OK#
        fc1.messages.take(); // clear UPDATE#

        fc1.disconnect();
        System.out.println(fc2.messages.take());
        assertTrue(fc2.messages.take()
                               .equals("DELETE#deleteCommand(1)"));

        fc2.disconnect();

    }
}