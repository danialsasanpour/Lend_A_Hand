package com.daniall.lend_a_hand.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.daniall.lend_a_hand.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.ChatLog;
import model.LastMessageAdapter;
import model.Message;
import model.User;

public class Chat_List extends AppCompatActivity implements View.OnClickListener {

    ListView lvChatList;
    ImageButton imageButtonHome, imageButtonMessage, imageButtonAccount;

    User currentUser;
    ArrayList<ChatLog> listOfChatLogs;

    FirebaseDatabase root = FirebaseDatabase.getInstance();
    DatabaseReference users = root.getReference("Users");
    DatabaseReference chatLogs = root.getReference("ChatLogs");

    Context context = this;
    LastMessageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        initialize();
    }

    private void initialize() {

        lvChatList = findViewById(R.id.lvChatList);
        currentUser = (User) getIntent().getExtras().getSerializable("currentUser");

        imageButtonHome = findViewById(R.id.imageButtonHome);
        imageButtonMessage = findViewById(R.id.imageButtonMessage);
        imageButtonAccount = findViewById(R.id.imageButtonAccount);

        imageButtonHome.setOnClickListener(this);
        imageButtonMessage.setOnClickListener(this);
        imageButtonAccount.setOnClickListener(this);

        chatLogs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listOfChatLogs = new ArrayList<ChatLog>();

                for (DataSnapshot ds : snapshot.getChildren())
                {
                    ChatLog chatLog = new ChatLog();
                    chatLog.setChatId(ds.child("chatId").getValue().toString());
                    chatLog.setUser1(ds.child("user1").getValue().toString());
                    chatLog.setUser2(ds.child("user2").getValue().toString());
                    chatLog.setLastMessage(ds.child("lastMessage").getValue().toString());
                    chatLog.setInterestedPost(ds.child("interestedPost").getValue().toString());

                    if (chatLog.getUser1().equals(currentUser.getUsername()) ||
                            chatLog.getUser2().equals(currentUser.getUsername()))
                    {

                        ArrayList<Message> listOfMessages = new ArrayList<Message>();
                        for (DataSnapshot oneMessage : ds.child("messages").getChildren())
                        {
                            Message message = new Message();
                            message.setMessage(oneMessage.child("message").getValue().toString());
                            message.setUsername(oneMessage.child("username").getValue().toString());
                            message.setTimeSent(Long.parseLong(oneMessage.child("timeSent").getValue().toString()));
                            listOfMessages.add(message);
                        }



                        chatLog.setMessages(listOfMessages);
                        listOfChatLogs.add(chatLog);
                    }

                }


                Collections.sort(listOfChatLogs, new Comparator<ChatLog>() {
                    @Override
                    public int compare(ChatLog o1, ChatLog o2) {
                        return o1.compareTo(o2);
                    }
                });

                adapter = new LastMessageAdapter(context, listOfChatLogs, currentUser);
                lvChatList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onClick(View v) {


        Intent intent = new Intent(this, Home.class);
        switch(v.getId())
        {
            case R.id.imageButtonHome:
                intent = new Intent(this, Home.class);
                intent.putExtra("currentUser", currentUser);
                break;
            case R.id.imageButtonMessage:
                intent = new Intent(this, Chat_List.class);
                intent.putExtra("currentUser", currentUser);
                break;
            case R.id.imageButtonAccount:
                intent = new Intent(this, Account_Details.class);
                intent.putExtra("currentUser", currentUser);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);

    }
}