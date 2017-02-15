/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import static server.ChatServer.users;

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
            for (User user : users) {
                if (!user.isConnected()) {
                    System.out.println("Removing: " + user.getUsername());
                    cs.deleteObserver(user);
                    users.remove(user);
                    cs.setChangedAndNotify(
                            new Notification(user, Notification.Type.DELETE));
                }
            }
            try {
                //System.out.println("Sleeping");
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
