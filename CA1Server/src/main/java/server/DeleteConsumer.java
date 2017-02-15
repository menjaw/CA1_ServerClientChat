/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jamie
 */
public class DeleteConsumer implements Runnable {

    ChatServer cs;

    public DeleteConsumer(ChatServer cs) {
        this.cs = cs;
    }

    @Override
    public void run() {
        while (true) {
            for (User user : ChatServer.users) {
                if (!user.isConnected()) {
                    cs.deleteObserver(user);
                    ChatServer.users.remove(user);
                    cs.setChangedAndNotify(
                            new Notification(user, Notification.Type.UPDATE));
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
