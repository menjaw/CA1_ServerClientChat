package io.skaarup;

/**
 * Created by Niki on 2017-02-16.
 *
 * @author Niki
 */
public class Notification {

    private Message message;
    private Type type;
    private String user;

    public Notification(Type type, String user) {
        this.type = type;
        this.user = user;
    }

    public Notification(Type type, Message message) {
        this.type = type;
        this.message = message;
    }

    public Message getMessage() {
        return message;
    }

    public Type getType() {
        return type;
    }

    public String getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "io.skaarup.Notification{" +
                "message=" + message +
                ", type=" + type +
                ", user='" + user + '\'' +
                '}';
    }

    public enum Type {
        MESSAGE,
        UPDATE,
        DELETE
    }
}
