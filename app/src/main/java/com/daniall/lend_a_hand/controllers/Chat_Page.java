package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import model.ChatLog;
import model.Message;
import model.MessagesAdapter;
import model.Post;
import model.User;

public class Chat_Page extends AppCompatActivity implements View.OnClickListener {

    TextView tvUsername;
    ImageView imageUserProfilePicture;
    ListView lvMessages;
    EditText edMessage;
    ImageButton imageButtonSend;

    User currentUser;
    Post currentPost;
    Context context = this;
    ChatLog foundChatLog;
    MessagesAdapter adapter;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference chatLogs = root.getReference("ChatLogs");
    DatabaseReference messages = root.getReference("Messages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_page);

        initialize();
    }

    private void initialize(){

        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");
        currentPost = (Post) getIntent().getExtras().getSerializable("currentPost");

        tvUsername = findViewById(R.id.tvUsername);
        imageUserProfilePicture = findViewById(R.id.imageUserProfilePicture);
        lvMessages = findViewById(R.id.lvMessages);
        edMessage = findViewById(R.id.edMessage);
        imageButtonSend = findViewById(R.id.imageButtonSend);

        tvUsername.setText(currentPost.getCreatedBy());

        imageButtonSend.setOnClickListener(this);


        chatLogs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ChatLog chatLog = new ChatLog();
                    chatLog.setChatId(ds.child("chatId").getValue().toString());
                    chatLog.setUser1(ds.child("user1").getValue().toString());
                    chatLog.setUser2(ds.child("user2").getValue().toString());


                    if (!chatLog.getUser1().equals(currentUser.getUsername()) && !chatLog.getUser2().equals(currentUser.getUsername()))
                        return;
                    if (!chatLog.getUser1().equals(currentPost.getCreatedBy()) && !chatLog.getUser2().equals(currentPost.getCreatedBy()))
                        return;

                    foundChatLog = chatLog;

                    ArrayList<Message> listOfMessages = new ArrayList<Message>();
                    for (DataSnapshot oneMessage : snapshot.child(foundChatLog.getChatId() + "/messages").getChildren())
                    {
                        Message message = new Message();
                        message.setMessage(oneMessage.child("message").getValue().toString());
                        message.setUsername(oneMessage.child("username").getValue().toString());
                        message.setTimeSent(Long.parseLong(oneMessage.child("timeSent").getValue().toString()));
                        listOfMessages.add(message);
                    }
                    foundChatLog.setMessages(listOfMessages);
                    displayMessages(foundChatLog.getMessages());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButtonSend:
                sendMessage();
                break;
        }
    }


    private void sendMessage() {

        if (edMessage.getText().toString().equals("")) {
            Toast.makeText(this, "Cannot send an empty message", Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = new Message(currentUser.getUsername(), edMessage.getText().toString(), System.currentTimeMillis());


        if (foundChatLog != null) {
            chatLogs.child(foundChatLog.getChatId() + "/messages/" + message.getTimeSent()).setValue(message);
            foundChatLog.getMessages().add(message);
            displayMessages(foundChatLog.getMessages());

            edMessage.setText("");
            return;
        }

        ChatLog chatLog = new ChatLog();
        chatLog.setUser1(currentUser.getUsername());
        chatLog.setUser2(currentPost.getCreatedBy());

        chatLogs.child(chatLog.getChatId()).setValue(chatLog);
        chatLogs.child(chatLog.getChatId() + "/messages/" + message.getTimeSent()).setValue(message);

        ArrayList<Message> newListOfMessages = new ArrayList<Message>();
        newListOfMessages.add(message);
        chatLog.setMessages(newListOfMessages);

        foundChatLog = chatLog;
        displayMessages(foundChatLog.getMessages());

        edMessage.setText("");
    }

    private void displayMessages(ArrayList<Message> listOfMessages) {
        adapter = new MessagesAdapter(context, listOfMessages, currentUser);
        //Toast.makeText(context, "" + adapter.getCount(), Toast.LENGTH_LONG).show();
        lvMessages.setAdapter(adapter);
        lvMessages.setSelection(listOfMessages.size() - 1);
    }



}