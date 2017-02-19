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


/**
 * @author Jamie
 */
public class MessageConsumer implements Runnable {

    public BlockingQueue<Message> messages = new ArrayBlockingQueue<>
            (128);

    private ChatServer cs;

    public MessageConsumer(ChatServer cs) {
        this.cs = cs;
    }

    @Override
    public void run() {
        Message msg;
        while (true) {
            try {
                msg = messages.take();
                if (!msg.hasReceiver()) { // ALL
                    for (User user : cs.users) {
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
