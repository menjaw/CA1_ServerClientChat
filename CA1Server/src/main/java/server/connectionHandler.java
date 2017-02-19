/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Jamie
 */
public class connectionHandler implements Runnable {

    Socket connection;
    ChatServer cs;

    public connectionHandler(Socket connection, ChatServer cs) {
        this.connection = connection;
        this.cs = cs;
    }

    @Override
    public void run() {
        try {
            OutputStream output = connection.getOutputStream();
            InputStream input = connection.getInputStream();

            // Read whatever comes in
            Scanner reader = new Scanner(input);
            String line;
            try {
                line = reader.nextLine();
            } catch (Exception ignored) {
                return;
            }

            // Print the same line we read to the client
            PrintStream writer = new PrintStream(output);
            boolean nameExists = false;
            String[] strings = line.split("#");

            if (strings.length >= 1
                    && strings[0].equalsIgnoreCase("LOGIN")) {

                for (User user : cs.users) {
                    if (user.getUsername().equalsIgnoreCase(strings[1])) {
                        writer.println("FAIL");
                        nameExists = true;
                        break;
                    }
                }
                if (nameExists) {
                    connection.close();
                    return;
                }
                User newGuy = new User(cs, connection, strings[1]);

                cs.users.add(newGuy);
                cs.addObserver(newGuy);
                cs.setChangedAndNotify(new Notification(newGuy, Notification
                        .Type.UPDATE));

                String okMsg = "OK";
                for (User user : cs.users) {
                    okMsg += "#" + user.getUsername();
                }
                newGuy.write(okMsg);

                cs.executor.execute(newGuy);

            }

        } catch (IOException ex) {
            Logger.getLogger(connectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
