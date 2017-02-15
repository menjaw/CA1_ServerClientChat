/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import static server.ChatServer.users;

/**
 *
 * @author Jamie
 */
public class MessageConsumer implements Runnable {

    public static BlockingQueue<Message> messages = new ArrayBlockingQueue<>(128);

    @Override
    public void run() {
        Message msg;
        while (true) {
            try {
                msg = messages.take();
                if (msg.getReceiver() == null) { // ALL
                    for (User user : users) {
                        user.write(msg.toString());
                    }
                } else { // Whisper
                    msg.getSender().write(msg.toString());
                    msg.getReceiver().write(msg.toString());
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
