package server;

/**
 * Created by Niki on 2017-02-15.
 *
 * @author Niki
 */
public class Notification {

    public User user;
    public NotificationType type;

    public Notification(User user, NotificationType type) {
        this.user = user;
        this.type = type;
    }

    public enum NotificationType {
        DELETE,
        UPDATE
    }

}
