/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.List;
import java.io.DataInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Scanner;

/**
 *
 * @author Bruger
 */
public class ClientControler extends Observable {

   
    private String ip = "localhost";
    private int port = 8081;

    Socket S;
    PrintWriter dout;
    Scanner scan;
    private ArrayList<String> userArray;
    private ArrayList<messages> userMessages;

    public ClientControler() {
        try {
            this.S = new Socket(ip, port);
            dout = new PrintWriter(S.getOutputStream(), true);
            scan = new Scanner(S.getInputStream());
            listenToInput();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void login(String name) {
        
        dout.println("LOGIN#" + name);
    }

    public void sendBesked(String user, String besked) {
        dout.println("MSG#" + user + "#" + besked);
    }

    public void listenToInput() {
        Thread input = new Thread(() -> {
            while (true) {
                String besked = scan.nextLine();
                String[] split = besked.split("#");
                if (split[0].equals("OK")) {
                    userArray = new ArrayList();
                    for (int i = 1; i < split.length; i++) {
                        userArray.add(split[i]);
                    }
                    setChanged();
                    notifyObservers("userListOpdate");
                }
                if (split[0].equals("DELETE")) {
                    userArray.remove(split[1]);
                    setChanged();
                    notifyObservers("userListOpdate");
                }
                if(split[0].equals("OPDATE")){
                    userArray.add(split[1]);
                    setChanged();
                    notifyObservers("userListOpdate");
                }
                if(split[0].equals("MSG")){
                    
                   userMessages.add(new messages(split[1],split[2]));
                    setChanged();
                    notifyObservers("userListOpdate");
                }
            }
        });

        input.start();
    }

    public ArrayList<String> getUserArray() {
        return userArray;
    }

    public ArrayList<messages> getUserMessages() {
        return userMessages;
    }
    

}
