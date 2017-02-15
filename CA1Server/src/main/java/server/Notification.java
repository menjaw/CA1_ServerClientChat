package server;

/**
 * Created by Niki on 2017-02-15.
 *
 * @author Niki
 */
public class Notification {

    public User user;
    public Type type;

    public Notification(User user, Type type) {
        this.user = user;
        this.type = type;
    }

    public enum Type {
        DELETE,
        UPDATE
    }

}
