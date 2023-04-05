package model;

import java.time.LocalDateTime;

public class Message {

    private String username;
    private String message;
    private long timeSent;

    public Message(String username, String message, long timeSent) {
        this.username = username;
        this.message = message;
        this.timeSent = timeSent;
    }

    public Message() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(long timeSent) {
        this.timeSent = timeSent;
    }

    @Override
    public String toString() {
        return "Message{" +
                "username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", timeSent=" + timeSent +
                '}';
    }
}
