package io.skaarup;

/**
 * Created by Niki on 2017-02-16.
 *
 * @author Niki
 */
public class Message {

    public String username;
    public String text;

    private boolean messagePrivate;

    /**
     * @param username sender/receiver
     * @param text     text
     */
    public Message(String username, String text) {
        this.username = username;
        this.text = text;

        messagePrivate = true;
    }

    /**
     * @param text text
     */
    public Message(String text) {
        this.text = text;

        messagePrivate = false;
    }

    boolean isMessagePrivate() {
        return messagePrivate;
    }

}
