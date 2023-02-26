package model;

import java.util.Arrays;
import java.util.UUID;

public class ChatLog {

    private String chatId;
    private String user1;
    private String user2;
    private Message[] messages;

    public ChatLog(String chatId, String user1, String user2, Message[] messages) {
        this.chatId = UUID.randomUUID().toString();
        this.user1 = user1;
        this.user2 = user2;
        this.messages = messages;
    }

    public ChatLog(){
        this.chatId = UUID.randomUUID().toString();
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public Message[] getMessages() {
        return messages;
    }

    public void setMessages(Message[] messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatLog{" +
                "chatId='" + chatId + '\'' +
                ", user1='" + user1 + '\'' +
                ", user2='" + user2 + '\'' +
                ", messages=" + Arrays.toString(messages) +
                '}';
    }
}
