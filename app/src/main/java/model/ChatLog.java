package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ChatLog implements Serializable {

    private String chatId;
    private String user1;
    private String user2;
    private ArrayList<Message> messages;
    private String lastMessage;
    private String interestedPost;

    public ChatLog(String chatId, String user1, String user2, ArrayList<Message> messages, String interestedPost) {
        this.chatId = UUID.randomUUID().toString();
        this.user1 = user1;
        this.user2 = user2;
        this.messages = messages;
        this.interestedPost = interestedPost;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getInterestedPost() {
        return interestedPost;
    }

    public void setInterestedPost(String interestedPost) {
        this.interestedPost = interestedPost;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatLog{" +
                "chatId='" + chatId + '\'' +
                ", user1='" + user1 + '\'' +
                ", user2='" + user2 + '\'' +
                '}';
    }

    public int compareTo(ChatLog o)
    {
        if (this.getMessages().get(this.getMessages().size()-1).getTimeSent() >
                o.getMessages().get(o.getMessages().size()-1).getTimeSent())
            return -1;
        return 1;

    }


}
