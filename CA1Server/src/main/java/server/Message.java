package server;

/**
 * Created by Niki on 2017-02-14.
 *
 * @author Niki
 */
public class Message {

    private User sender;
    private User receiver = null;
    private String data;
    private ChatServer cs;

    public Message(User sender, String s) {
        this.sender = sender;
        cs = sender.cs;
        String[] strings = s.split("#");
        setReceiver(strings);
        setData(strings);
    }

    public Message(User sender, String[] strings) {
        this.sender = sender;
        setReceiver(strings);
        setData(strings);
    }

    private void setData(String[] strings) {
        if (strings.length >= 3) {
            data = "";
            for (int i = 2; i < strings.length; i++)
                data += strings[i];
        } else {
            data = "Malformed message";
        }
    }

    private void setReceiver(String[] strings) {
        if (strings.length <= 1) return;
        if (strings[1].equalsIgnoreCase("ALL")) return;

        receiver = findUser(strings[1]);
    }

    private User findUser(String s) {
        for (int i = 0; i < cs.users.size(); i++) {
            if(cs.users.get(i).getUsername().equalsIgnoreCase(s))
                return cs.users.get(i);
        }

        return null;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getData() {
        return data;
    }

    public boolean hasReceiver() {
        return receiver != null;
    }

    @Override
    public String toString() {
        return "MSG#" + sender.getUsername() + "#" + data;
    }

}
